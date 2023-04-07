package photo.service.api;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import sashkir7.grpc.UserdataServiceGrpc;
import sashkir7.grpc.UsernameRequest;
import sashkir7.grpc.Users;

@Component
public class UserdataGrpcClient {

    @GrpcClient("grpcUserdataClient")
    private UserdataServiceGrpc.UserdataServiceBlockingStub userdataServiceBlockingStub;

    public Users getFriends(UsernameRequest request) {
        return userdataServiceBlockingStub.getFriends(request);
    }

}
