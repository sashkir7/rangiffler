package userdata.service;

import guru.qa.grpc.niffler.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import userdata.data.FriendStatus;
import userdata.data.UserEntity;
import userdata.data.repository.UserRepository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static userdata.data.FriendStatus.NOT_FRIEND;

@GrpcService
public class GrpcUserService extends UserdataServiceGrpc.UserdataServiceImplBase {

    private final UserRepository userRepository;

    @Autowired
    public GrpcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getCurrentUser(UsernameRequest request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        responseObserver.onNext(convertFromEntity(userEntity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateCurrentUser(User request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .setFirstname(request.getFirstname())
                .setLastname(request.getLastname())
                .setAvatar(request.getAvatarBytes().toByteArray());
        responseObserver.onNext(convertFromEntity(userRepository.save(userEntity)));
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(UsernameRequest request, StreamObserver<GetAllUsersResponse> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        Set<UserEntity> allUsersWithoutCurrentUser = userRepository.findAllByUsernameNot(request.getUsername());

        GetAllUsersResponse.Builder responseBuilder = GetAllUsersResponse.newBuilder();
        for (FriendStatus status : FriendStatus.values()) {
            Set<UserEntity> usersByStatus = userEntity.getRelationshipUsersByStatus(status);
            allUsersWithoutCurrentUser.removeAll(usersByStatus);
            responseBuilder.putUsers(status.toString(), convertFromEntities(usersByStatus));
        }

        responseBuilder.putUsers(NOT_FRIEND.toString(), convertFromEntities(allUsersWithoutCurrentUser));
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private User convertFromEntity(UserEntity entity) {
        User.Builder builder = User.newBuilder()
                .setId(entity.getId().toString())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname())
                .setLastname(entity.getLastname());
        if (entity.getAvatarAsString() != null) {
            builder.setAvatar(entity.getAvatarAsString());
        }
        return builder.build();
    }

    private Users convertFromEntities(Collection<UserEntity> entities) {
        Set<User> users = entities.stream().map(this::convertFromEntity).collect(Collectors.toSet());
        return Users.newBuilder().addAllUsers(users).build();
    }

}
