package userdata.service;

import guru.qa.grpc.niffler.grpc.UserResponse;
import guru.qa.grpc.niffler.grpc.UserdataServiceGrpc;
import guru.qa.grpc.niffler.grpc.UsernameRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import userdata.data.UserEntity;
import userdata.data.repository.UserRepository;
import userdata.exception.UserNotFoundGrpcException;

@GrpcService
public class GrpcUserService extends UserdataServiceGrpc.UserdataServiceImplBase {

    private final UserRepository userRepository;

    @Autowired
    public GrpcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getCurrentUser(UsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        if (userEntity == null) {
            responseObserver.onError(new UserNotFoundGrpcException(request.getUsername()));
        } else {
            responseObserver.onNext(convertFromEntity(userEntity));
            responseObserver.onCompleted();
        }
    }

    private UserResponse convertFromEntity(UserEntity entity) {
        UserResponse.Builder builder = UserResponse.newBuilder()
                .setId(entity.getId().toString())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname())
                .setLastname(entity.getLastname());
        if (entity.getAvatarAsString() != null) {
            builder.setAvatar(entity.getAvatarAsString());
        }
        return builder.build();
    }

}
