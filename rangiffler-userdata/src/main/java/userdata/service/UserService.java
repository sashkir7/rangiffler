package userdata.service;

import jakarta.annotation.Nonnull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userdata.data.FriendStatus;
import userdata.data.UserEntity;
import userdata.data.UsersRelationshipEntity;
import userdata.data.repository.UserRepository;
import userdata.model.UserDto;

import javax.management.InstanceAlreadyExistsException;
import java.util.*;
import java.util.stream.Collectors;

import static userdata.data.FriendStatus.*;

@Component
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nonnull UserDto getByUsername(@Nonnull String username) {
        // ToDo Обработать исключение, когда пользователь не найден
        UserEntity entity = userRepository.findByUsername(username);
        return UserDto.fromEntity(entity);
    }

    public @Nonnull UserDto updateCurrentUser(@Nonnull UserDto userDto) {
        // ToDo Также добавить обработку исключений
        UserEntity entity = userRepository.save(UserEntity.fromDto(userDto));
        return UserDto.fromEntity(entity);
    }

    @RabbitListener(queues = {"q.userdata-registration"})
    public void saveRegistrationUser(@Nonnull String userJson) throws InstanceAlreadyExistsException {
        // ToDo Если возникает ошибка, то сообщение перекладывается в эту же самую очередь
        //  ---> Тем самым получаем бесконечный цикл и кабзду на выходе.
        //  ---> Реализовать, чтобы в случае ошибки сообщение перекладывалось в ошибочную очередь
        UserDto userDto = UserDto.fromJson(userJson);

        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new InstanceAlreadyExistsException(userDto.getUsername());
        }

        UserEntity entity = UserEntity.builder()
                .username(userDto.getUsername())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .build();
        userRepository.save(entity);
    }

    public @Nonnull Map<FriendStatus, Set<UserDto>> getAllUsers(@Nonnull String username) {
        UserEntity currentUser = userRepository.findByUsername(username);
        Set<UserEntity> allUsersWithoutCurrentUser = userRepository.findAllByUsernameNot(username);

        Map<FriendStatus, Set<UserDto>> allUsers = new HashMap<>();
        for (FriendStatus status : FriendStatus.values()) {
            Set<UserEntity> entities = currentUser.getRelationshipUsersByStatus(status);
            allUsersWithoutCurrentUser.removeAll(entities);
            allUsers.put(status, convertUserEntitiesToDtos(entities));
        }

        allUsers.put(NOT_FRIEND, convertUserEntitiesToDtos(allUsersWithoutCurrentUser));
        return allUsers;
    }

    public void inviteToFriends(@Nonnull String ownUsername, @Nonnull String friendUsername) {
        UserEntity ownUser = userRepository.findByUsername(ownUsername);
        UserEntity friendUser = userRepository.findByUsername(friendUsername);

        UsersRelationshipEntity build = UsersRelationshipEntity.builder()
                .user(ownUser)
                .friend(friendUser)
                .relationship(INVITATION_SENT)
                .build();
        ownUser.addUserRelationship(build);

        UsersRelationshipEntity a = UsersRelationshipEntity.builder()
                .user(friendUser)
                .friend(ownUser)
                .relationship(INVITATION_RECEIVED)
                .build();
        friendUser.addUserRelationship(a);

        userRepository.save(ownUser);
        userRepository.save(friendUser);
    }

    public void submitFriend(String username, UserDto friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendUser = userRepository.findByUsername(friend.getUsername());

        UsersRelationshipEntity relationship = currentUser.getRelationshipUsers()
                .stream()
                .filter(f -> f.getFriend().getId().equals(friendUser.getId()))
                .filter(f -> f.getRelationship().equals(INVITATION_RECEIVED))
                .findFirst().orElseThrow();
        relationship.setRelationship(FRIEND);

        UsersRelationshipEntity relationship1 = friendUser.getRelationshipUsers().stream()
                .filter(f -> f.getFriend().getId().equals(currentUser.getId()))
                .filter(f -> f.getRelationship().equals(INVITATION_SENT))
                .findFirst().orElseThrow();
        relationship1.setRelationship(FRIEND);

        userRepository.save(currentUser);
        userRepository.save(friendUser);
    }

    public void declineFriend(String username, UserDto friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendUser = userRepository.findByUsername(friend.getUsername());

        UsersRelationshipEntity relationship = currentUser.getRelationshipUsers()
                .stream()
                .filter(f -> f.getFriend().getId().equals(friendUser.getId()))
                .filter(f -> f.getRelationship().equals(INVITATION_RECEIVED))
                .findFirst().orElseThrow();
        currentUser.getRelationshipUsers().remove(relationship);

        UsersRelationshipEntity relationship1 = friendUser.getRelationshipUsers().stream()
                .filter(f -> f.getFriend().getId().equals(currentUser.getId()))
                .filter(f -> f.getRelationship().equals(INVITATION_SENT))
                .findFirst().orElseThrow();
        friendUser.getRelationshipUsers().remove(relationship1);

        userRepository.save(currentUser);
        userRepository.save(friendUser);
    }

    public void removeFriend(String username, UserDto friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendUser = userRepository.findByUsername(friend.getUsername());

        UsersRelationshipEntity relationship = currentUser.getRelationshipUsers()
                .stream()
                .filter(f -> f.getFriend().getId().equals(friendUser.getId()))
                .filter(f -> f.getRelationship().equals(FRIEND))
                .findFirst().orElseThrow();
        currentUser.getRelationshipUsers().remove(relationship);

        UsersRelationshipEntity relationship1 = friendUser.getRelationshipUsers().stream()
                .filter(f -> f.getFriend().getId().equals(currentUser.getId()))
                .filter(f -> f.getRelationship().equals(FRIEND))
                .findFirst().orElseThrow();
        friendUser.getRelationshipUsers().remove(relationship1);

        userRepository.save(currentUser);
        userRepository.save(friendUser);
    }

    private Set<UserDto> convertUserEntitiesToDtos(Set<UserEntity> entities) {
        return entities.stream().map(UserDto::fromEntity).collect(Collectors.toSet());
    }

}
