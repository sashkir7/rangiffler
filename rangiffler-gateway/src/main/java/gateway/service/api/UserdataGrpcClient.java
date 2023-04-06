package gateway.service.api;

import gateway.model.PartnerStatus;
import gateway.model.UserDto;
import guru.qa.grpc.niffler.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserdataGrpcClient {

    @GrpcClient("grpcCurrencyClient")
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

}
