package userdata.service;

import guru.qa.grpc.niffler.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import userdata.data.FriendStatus;
import userdata.data.UserEntity;
import userdata.data.UsersRelationshipEntity;
import userdata.data.repository.UserRepository;
import userdata.exception.RelationshipUsersFoundException;
import userdata.exception.RelationshipUsersNotFoundException;
import userdata.exception.RelationshipWithMyselfException;

import java.util.*;
import java.util.stream.Collectors;

import static userdata.data.FriendStatus.*;

@GrpcService
public class GrpcUsersService extends UserdataServiceGrpc.UserdataServiceImplBase {

    private final UserRepository userRepository;

    @Autowired
    public GrpcUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getCurrentUser(UsernameRequest request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        responseObserver.onNext(convertToUserFromEntity(userEntity));
        responseObserver.onCompleted();
    }

    @Override
    public void updateCurrentUser(User request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .setFirstname(request.getFirstname())
                .setLastname(request.getLastname())
                .setAvatar(request.getAvatarBytes().toByteArray());
        responseObserver.onNext(convertToUserFromEntity(userRepository.save(userEntity)));
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
            responseBuilder.putUsers(status.toString(), convertToUsersFromEntities(usersByStatus));
        }

        responseBuilder.putUsers(NOT_FRIEND.toString(), convertToUsersFromEntities(allUsersWithoutCurrentUser));
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void inviteToFriends(RelationshipUsersRequest request,
                                StreamObserver<RelationshipsResponse> responseObserver) {
        checkThatUserHaveNotRelationshipWithMyself(request.getUsername(), request.getPartner());
        UserEntity currentUser = userRepository.findByUsername(request.getUsername());
        UserEntity partnerUser = userRepository.findByUsername(request.getPartner().getUsername());

        checkThatUserHaveNotRelationship(currentUser, partnerUser);
        checkThatUserHaveNotRelationship(partnerUser, currentUser);

        UsersRelationshipEntity currentUserRelationship = createUsersRelationship(currentUser, partnerUser, INVITATION_SENT);
        currentUser.addUserRelationship(currentUserRelationship);
        UsersRelationshipEntity partnerUserRelationship = createUsersRelationship(partnerUser, currentUser, INVITATION_RECEIVED);
        partnerUser.addUserRelationship(partnerUserRelationship);

        userRepository.save(currentUser);
        userRepository.save(partnerUser);

        responseObserver.onNext(convertToRelationshipsFromEntities(currentUserRelationship, partnerUserRelationship));
        responseObserver.onCompleted();
    }

    @Override
    public void submitFriends(RelationshipUsersRequest request,
                              StreamObserver<RelationshipsResponse> responseObserver) {
        checkThatUserHaveNotRelationshipWithMyself(request.getUsername(), request.getPartner());
        UserEntity currentUser = userRepository.findByUsername(request.getUsername());
        UserEntity partnerUser = userRepository.findByUsername(request.getPartner().getUsername());

        UsersRelationshipEntity currentUserRelationship = checkThatUserHaveRelationship(currentUser, partnerUser, INVITATION_RECEIVED);
        UsersRelationshipEntity partnerUserRelationship = checkThatUserHaveRelationship(partnerUser, currentUser, INVITATION_SENT);

        currentUserRelationship.setRelationship(FRIEND);
        partnerUserRelationship.setRelationship(FRIEND);

        userRepository.save(currentUser);
        userRepository.save(partnerUser);

        responseObserver.onNext(convertToRelationshipsFromEntities(currentUserRelationship, partnerUserRelationship));
        responseObserver.onCompleted();
    }

    private User convertToUserFromEntity(UserEntity entity) {
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

    private Users convertToUsersFromEntities(Collection<UserEntity> entities) {
        Set<User> users = entities.stream().map(this::convertToUserFromEntity).collect(Collectors.toSet());
        return Users.newBuilder().addAllUsers(users).build();
    }

    private UsersRelationshipEntity createUsersRelationship(UserEntity currentUser,
                                                            UserEntity partner,
                                                            FriendStatus relationship) {
        return UsersRelationshipEntity.builder()
                .user(currentUser)
                .friend(partner)
                .relationship(relationship)
                .build();
    }

    public RelationshipsResponse convertToRelationshipsFromEntities(UsersRelationshipEntity... entities) {
        List<RelationshipResponse> relationships = Arrays.stream(entities)
                .map(rel -> RelationshipResponse.newBuilder()
                        .setUser(convertToUserFromEntity(rel.getUser()))
                        .setPartner(convertToUserFromEntity(rel.getFriend()))
                        .setRelationship(rel.getRelationship().toString())
                        .build())
                .toList();
        return RelationshipsResponse.newBuilder().addAllRelationships(relationships).build();
    }

    private void checkThatUserHaveNotRelationshipWithMyself(String username, User partner) {
        if (username.equals(partner.getUsername())) {
            throw new RelationshipWithMyselfException(username);
        }
    }

    private void checkThatUserHaveNotRelationship(UserEntity currentUser, UserEntity partnerUser) {
        Optional<UsersRelationshipEntity> relationship = currentUser.findRelationship(partnerUser);
        if (relationship.isPresent()) {
            throw new RelationshipUsersFoundException(relationship.get());
        }
    }

    public UsersRelationshipEntity checkThatUserHaveRelationship(UserEntity currentUser, UserEntity partnerUser, FriendStatus status) {
        Optional<UsersRelationshipEntity> relationship = currentUser.findRelationship(partnerUser, status);
        if (relationship.isEmpty()) {
            throw new RelationshipUsersNotFoundException(currentUser.getUsername(), partnerUser.getUsername(), status);
        }
        return relationship.get();
    }

}
