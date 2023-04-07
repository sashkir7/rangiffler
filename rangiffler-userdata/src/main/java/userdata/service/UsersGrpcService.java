package userdata.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import sashkir7.grpc.*;
import userdata.data.PartnerStatus;
import userdata.data.UserEntity;
import userdata.data.UsersRelationshipEntity;
import userdata.data.repository.UserRepository;
import userdata.exception.RelationshipUsersFoundException;
import userdata.exception.RelationshipUsersNotFoundException;
import userdata.exception.RelationshipWithMyselfException;

import java.util.*;
import java.util.stream.Collectors;

import static userdata.data.PartnerStatus.*;

@GrpcService
public class UsersGrpcService extends UserdataServiceGrpc.UserdataServiceImplBase {

    private final UserRepository userRepository;

    @Autowired
    public UsersGrpcService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getCurrentUser(UsernameRequest request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        responseObserver.onNext(userEntity.toGrpc());
        responseObserver.onCompleted();
    }

    @Override
    public void updateCurrentUser(User request, StreamObserver<User> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .setFirstname(request.getFirstname())
                .setLastname(request.getLastname())
                .setAvatar(request.getAvatarBytes().toByteArray());
        responseObserver.onNext(userEntity.toGrpc());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(UsernameRequest request, StreamObserver<GetAllUsersResponse> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        Set<UserEntity> allUsersWithoutCurrentUser = userRepository.findAllByUsernameNot(request.getUsername());

        GetAllUsersResponse.Builder responseBuilder = GetAllUsersResponse.newBuilder();
        for (PartnerStatus status : PartnerStatus.values()) {
            Set<UserEntity> usersByStatus = userEntity.getRelationshipUsersByStatus(status);
            allUsersWithoutCurrentUser.removeAll(usersByStatus);
            responseBuilder.putUsers(status.toString(), convertToUsers(usersByStatus));
        }

        responseBuilder.putUsers(NOT_FRIEND.toString(), convertToUsers(allUsersWithoutCurrentUser));
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getFriends(UsernameRequest request, StreamObserver<Users> responseObserver) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());
        responseObserver.onNext(convertToUsers(userEntity.getRelationshipUsersByStatus(FRIEND)));
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

        responseObserver.onNext(convertToRelationships(currentUserRelationship, partnerUserRelationship));
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

        currentUserRelationship.setStatus(FRIEND);
        partnerUserRelationship.setStatus(FRIEND);

        userRepository.save(currentUser);
        userRepository.save(partnerUser);

        responseObserver.onNext(convertToRelationships(currentUserRelationship, partnerUserRelationship));
        responseObserver.onCompleted();
    }

    @Override
    public void declineFriend(RelationshipUsersRequest request,
                              StreamObserver<Empty> responseObserver) {
        checkThatUserHaveNotRelationshipWithMyself(request.getUsername(), request.getPartner());
        UserEntity currentUser = userRepository.findByUsername(request.getUsername());
        UserEntity partnerUser = userRepository.findByUsername(request.getPartner().getUsername());

        currentUser.removeRelationship(checkThatUserHaveRelationship(currentUser, partnerUser, INVITATION_RECEIVED));
        partnerUser.removeRelationship(checkThatUserHaveRelationship(partnerUser, currentUser, INVITATION_SENT));

        userRepository.save(currentUser);
        userRepository.save(partnerUser);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void removeFriend(RelationshipUsersRequest request,
                             StreamObserver<Empty> responseObserver) {
        checkThatUserHaveNotRelationshipWithMyself(request.getUsername(), request.getPartner());
        UserEntity currentUser = userRepository.findByUsername(request.getUsername());
        UserEntity partnerUser = userRepository.findByUsername(request.getPartner().getUsername());

        currentUser.removeRelationship(checkThatUserHaveRelationship(currentUser, partnerUser, FRIEND));
        partnerUser.removeRelationship(checkThatUserHaveRelationship(partnerUser, currentUser, FRIEND));

        userRepository.save(currentUser);
        userRepository.save(partnerUser);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private UsersRelationshipEntity createUsersRelationship(UserEntity currentUser,
                                                            UserEntity partner,
                                                            PartnerStatus relationship) {
        return UsersRelationshipEntity.builder()
                .user(currentUser)
                .partner(partner)
                .status(relationship)
                .build();
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

    public UsersRelationshipEntity checkThatUserHaveRelationship(UserEntity currentUser, UserEntity partnerUser, PartnerStatus status) {
        Optional<UsersRelationshipEntity> relationship = currentUser.findRelationship(partnerUser, status);
        if (relationship.isEmpty()) {
            throw new RelationshipUsersNotFoundException(currentUser.getUsername(), partnerUser.getUsername(), status);
        }
        return relationship.get();
    }

    private Users convertToUsers(Collection<UserEntity> entities) {
        return Users.newBuilder()
                .addAllUsers(entities.stream()
                        .map(UserEntity::toGrpc)
                        .collect(Collectors.toSet())
                ).build();
    }

    public RelationshipsResponse convertToRelationships(UsersRelationshipEntity... entities) {
        return RelationshipsResponse.newBuilder()
                .addAllRelationships(Arrays.stream(entities)
                        .map(UsersRelationshipEntity::toGrpc)
                        .collect(Collectors.toSet())
                ).build();
    }

}
