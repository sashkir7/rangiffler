package gateway.service.api;

import gateway.model.PartnerStatus;
import gateway.model.UserDto;
import gateway.model.UsersRelationshipDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import sashkir7.grpc.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserdataGrpcClient {

    @GrpcClient("grpcUserdataClient")
    private UserdataServiceGrpc.UserdataServiceBlockingStub userdataServiceBlockingStub;

    public UserDto getCurrentUser(String username) {
        UsernameRequest request = UsernameRequest.newBuilder().setUsername(username).build();
        return convertToUserDto(userdataServiceBlockingStub.getCurrentUser(request));
    }

    public UserDto updateCurrentUser(UserDto userDto) {
        return convertToUserDto(userdataServiceBlockingStub.updateCurrentUser(convertToUserGrpc(userDto)));
    }

    public Map<PartnerStatus, Set<UserDto>> getAllUsers(String username) {
        UsernameRequest request = UsernameRequest.newBuilder().setUsername(username).build();
        Map<String, Users> allGrpcUsers = userdataServiceBlockingStub.getAllUsers(request).getUsersMap();

        Map<PartnerStatus, Set<UserDto>> allUsers = new HashMap<>();
        for (String partnerStatus : allGrpcUsers.keySet()) {
            Users users = allGrpcUsers.get(partnerStatus);
            allUsers.put(PartnerStatus.valueOf(partnerStatus), convertToUserDtos(users));
        }
        return allUsers;
    }

    public Set<UserDto> getFriends(String username) {
        UsernameRequest request = UsernameRequest.newBuilder().setUsername(username).build();
        return convertToUserDtos(userdataServiceBlockingStub.getFriends(request));
    }

    public List<UsersRelationshipDto> inviteToFriends(String username, UserDto partnerDto) {
        RelationshipUsersRequest request = getRelationshipRequest(username, partnerDto);
        return convertToUsersRelationshipDtos(userdataServiceBlockingStub.inviteToFriends(request));
    }

    public List<UsersRelationshipDto> submitFriends(String username, UserDto partnerDto) {
        RelationshipUsersRequest request = getRelationshipRequest(username, partnerDto);
        return convertToUsersRelationshipDtos(userdataServiceBlockingStub.submitFriends(request));
    }

    public void declineFriend(String username, UserDto partnerDto) {
        RelationshipUsersRequest request = getRelationshipRequest(username, partnerDto);
        userdataServiceBlockingStub.declineFriend(request);
    }

    public void removeFriend(String username, UserDto partnerDto) {
        RelationshipUsersRequest request = getRelationshipRequest(username, partnerDto);
        userdataServiceBlockingStub.removeFriend(request);
    }

    private User convertToUserGrpc(UserDto userDto) {
        return User.newBuilder()
                .setId(userDto.getId().toString())
                .setUsername(userDto.getUsername())
                .setFirstname(userDto.getFirstname())
                .setLastname(userDto.getLastname())
                .setAvatar(userDto.getAvatar() == null ? "" : userDto.getAvatar())
                .build();
    }

    private UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .avatar(user.getAvatar())
                .build();
    }

    private Set<UserDto> convertToUserDtos(Users users) {
        return users.getUsersList().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toSet());
    }

    private UsersRelationshipDto convertToUsersRelationshipDto(RelationshipResponse relationshipResponse) {
        return UsersRelationshipDto.builder()
                .user(convertToUserDto(relationshipResponse.getUser()))
                .partner(convertToUserDto(relationshipResponse.getPartner()))
                .status(PartnerStatus.valueOf(relationshipResponse.getStatus()))
                .build();
    }

    private List<UsersRelationshipDto> convertToUsersRelationshipDtos(RelationshipsResponse relationshipsResponse) {
        return relationshipsResponse.getRelationshipsList().stream()
                .map(this::convertToUsersRelationshipDto)
                .toList();
    }

    private RelationshipUsersRequest getRelationshipRequest(String username, UserDto partnerDto) {
        return RelationshipUsersRequest.newBuilder()
                .setUsername(username)
                .setPartner(convertToUserGrpc(partnerDto))
                .build();
    }

}
