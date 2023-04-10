package gateway.service.api;

import gateway.model.PartnerStatus;
import gateway.model.UserDto;
import gateway.model.UsersRelationshipDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import sashkir7.grpc.*;

import java.util.*;

@Component
public class UserdataGrpcClient {

    @GrpcClient("grpcUserdataClient")
    private UserdataServiceGrpc.UserdataServiceBlockingStub userdataServiceBlockingStub;

    public UserDto getCurrentUser(String username) {
        User user = userdataServiceBlockingStub.getCurrentUser(getUsernameRequest(username));
        return UserDto.fromGrpc(user);
    }

    public UserDto updateCurrentUser(UserDto userDto) {
        User user = userdataServiceBlockingStub.updateCurrentUser(userDto.toGrpc());
        return UserDto.fromGrpc(user);
    }

    public Map<PartnerStatus, Set<UserDto>> getAllUsers(String username) {
        Map<String, Users> grpcUsers = userdataServiceBlockingStub.getAllUsers(getUsernameRequest(username)).getUsersMap();
        Map<PartnerStatus, Set<UserDto>> allUsers = new HashMap<>();
        for (String partnerStatus : grpcUsers.keySet()) {
            Users users = grpcUsers.get(partnerStatus);
            allUsers.put(PartnerStatus.valueOf(partnerStatus), UserDto.fromGrpc(users));
        }
        return allUsers;
    }

    public Set<UserDto> getFriends(String username) {
        Users users = userdataServiceBlockingStub.getFriends(getUsernameRequest(username));
        return UserDto.fromGrpc(users);
    }

    public UsersRelationshipDto inviteToFriends(String username, UserDto partnerDto) {
        RelationshipResponse relationship = userdataServiceBlockingStub.inviteToFriends(
                getRelationshipRequest(username, partnerDto));
        return UsersRelationshipDto.fromGrpc(relationship);
    }

    public UsersRelationshipDto submitFriends(String username, UserDto partnerDto) {
        RelationshipResponse relationship = userdataServiceBlockingStub.submitFriends(
                getRelationshipRequest(username, partnerDto));
        return UsersRelationshipDto.fromGrpc(relationship);
    }

    public void declineFriend(String username, UserDto partnerDto) {
        userdataServiceBlockingStub.declineFriend(getRelationshipRequest(username, partnerDto));
    }

    public void removeFriend(String username, UserDto partnerDto) {
        userdataServiceBlockingStub.removeFriend(getRelationshipRequest(username, partnerDto));
    }

    private UsernameRequest getUsernameRequest(String username) {
        return UsernameRequest.newBuilder().setUsername(username).build();
    }

    private RelationshipUsersRequest getRelationshipRequest(String username, UserDto partnerDto) {
        return RelationshipUsersRequest.newBuilder()
                .setUsername(username)
                .setPartner(partnerDto.toGrpc())
                .build();
    }

}
