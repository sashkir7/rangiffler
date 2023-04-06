package gateway.service.api;

import com.google.protobuf.Empty;
import gateway.model.UserDto;
import guru.qa.grpc.niffler.grpc.User;
import guru.qa.grpc.niffler.grpc.UserdataServiceGrpc;
import guru.qa.grpc.niffler.grpc.UsernameRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserdataGrpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(UserdataGrpcClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("grpcCurrencyClient")
    private UserdataServiceGrpc.UserdataServiceBlockingStub userdataServiceBlockingStub;

    public UserDto getCurrentUser(String username) {
        UsernameRequest request = UsernameRequest.newBuilder().setUsername(username).build();
        return convertToUserDto(userdataServiceBlockingStub.getCurrentUser(request));
    }

    public UserDto updateCurrentUser(UserDto userDto) {
        return convertToUserDto(userdataServiceBlockingStub.updateCurrentUser(convertToUserGrpc(userDto)));
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

}
