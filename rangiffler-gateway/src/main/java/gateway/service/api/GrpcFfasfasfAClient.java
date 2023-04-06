package gateway.service.api;

import com.google.protobuf.Empty;
import gateway.model.UserJson;
import guru.qa.grpc.niffler.grpc.User;
import guru.qa.grpc.niffler.grpc.UserdataServiceGrpc;
import guru.qa.grpc.niffler.grpc.UsernameRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GrpcFfasfasfAClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcFfasfasfAClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("grpcCurrencyClient")
    private UserdataServiceGrpc.UserdataServiceBlockingStub userdataServiceBlockingStub;

    public UserJson getCurrentUser(String username) {
        UsernameRequest build = UsernameRequest.newBuilder().setUsername(username).build();
        User currentUser = userdataServiceBlockingStub.getCurrentUser(build);

        return UserJson.builder()
                .id(UUID.fromString(currentUser.getId()))
                .username(currentUser.getUsername())
                .firstname(currentUser.getFirstname())
                .lastname(currentUser.getLastname())
                .avatar(currentUser.getAvatar())
                .build();
    }

}
