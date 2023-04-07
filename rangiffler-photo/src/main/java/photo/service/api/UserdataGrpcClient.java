package photo.service.api;

import guru.qa.grpc.niffler.grpc.UserdataServiceGrpc;
import guru.qa.grpc.niffler.grpc.UsernameRequest;
import guru.qa.grpc.niffler.grpc.Users;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class UserdataGrpcClient {

    @GrpcClient("grpcUserdataClient")
    private UserdataServiceGrpc.UserdataServiceBlockingStub userdataServiceBlockingStub;

    public Users getFriends(UsernameRequest request) {
        return userdataServiceBlockingStub.getFriends(request);
    }

}
