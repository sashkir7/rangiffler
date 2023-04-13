package api;

import model.UserModel;
import sashkir7.grpc.*;

public final class UserdataGrpcApi extends BaseGrpcApi {

    private final UserdataServiceGrpc.UserdataServiceBlockingStub blockingStub;

    public UserdataGrpcApi() {
        super("localhost", 9002);
        blockingStub = UserdataServiceGrpc.newBlockingStub(channel);
    }

    public User getUserByUsername(String username) {
        return blockingStub.getCurrentUser(getUsernameRequest(username));
    }

    public User addUser(User user) {
        return blockingStub.addUser(user);
    }

    public User updateUser(User user) {
        return blockingStub.updateCurrentUser(user);
    }

    public User updateUser(UserModel user) {
        return updateUser(user.toGrpc());
    }

    public void deleteUser(String username) {
        blockingStub.deleteUser(getUsernameRequest(username));
    }

    public GetAllUsersResponse getAllUsers(String username) {
        return blockingStub.getAllUsers(getUsernameRequest(username));
    }

    public Users getFriends(String username) {
        return blockingStub.getFriends(getUsernameRequest(username));
    }

    public RelationshipResponse inviteToFriends(String username, User partner) {
        return blockingStub.inviteToFriends(getRelationshipUsersRequest(username, partner));
    }

    public RelationshipResponse inviteToFriends(UserModel user, UserModel partner) {
        return inviteToFriends(user.getUsername(), partner.toGrpc());
    }

    public RelationshipResponse submitFriends(String username, User partner) {
        return blockingStub.submitFriends(getRelationshipUsersRequest(username, partner));
    }

    public RelationshipResponse submitFriends(UserModel user, UserModel partner) {
        return submitFriends(user.getUsername(), partner.toGrpc());
    }

    public void declineFriend(String username, User partner) {
        blockingStub.declineFriend(getRelationshipUsersRequest(username, partner));
    }

    public void declineFriend(String username, UserModel partner) {
        declineFriend(username, partner.toGrpc());
    }

    public void removeFriend(String username, User partner) {
        blockingStub.removeFriend(getRelationshipUsersRequest(username, partner));
    }

    public void removeFriend(String username, UserModel partner) {
        removeFriend(username, partner.toGrpc());
    }

    private RelationshipUsersRequest getRelationshipUsersRequest(String username, User partner) {
        return RelationshipUsersRequest.newBuilder()
                .setUsername(username)
                .setPartner(partner)
                .build();
    }

}
