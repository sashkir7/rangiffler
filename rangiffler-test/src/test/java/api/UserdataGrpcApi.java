package api;

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

    public GetAllUsersResponse getAllUsers(String username) {
        return blockingStub.getAllUsers(getUsernameRequest(username));
    }

    public Users getFriends(String username) {
        return blockingStub.getFriends(getUsernameRequest(username));
    }

    public RelationshipsResponse inviteToFriends(String username, User partner) {
        return blockingStub.inviteToFriends(getRelationshipUsersRequest(username, partner));
    }

    public RelationshipsResponse submitFriends(String username, User partner) {
        return blockingStub.submitFriends(getRelationshipUsersRequest(username, partner));
    }

    public void declineFriend(String username, User partner) {
        blockingStub.declineFriend(getRelationshipUsersRequest(username, partner));
    }

    public void removeFriend(String username, User partner) {
        blockingStub.removeFriend(getRelationshipUsersRequest(username, partner));
    }

    private RelationshipUsersRequest getRelationshipUsersRequest(String username, User partner) {
        return RelationshipUsersRequest.newBuilder()
                .setUsername(username)
                .setPartner(partner)
                .build();
    }

}
