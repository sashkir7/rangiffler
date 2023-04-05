package userdata.service;

import jakarta.annotation.Nonnull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userdata.data.FriendStatus;
import userdata.data.UserEntity;
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

    public @Nonnull UserDto getCurrentUser(@Nonnull String username) {
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

    public @Nonnull Map<FriendStatus, Set<UserDto>> getAllUsers(String username) {
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

    private Set<UserDto> convertUserEntitiesToDtos(Set<UserEntity> entities) {
        return entities.stream().map(UserDto::fromEntity).collect(Collectors.toSet());
    }

}
