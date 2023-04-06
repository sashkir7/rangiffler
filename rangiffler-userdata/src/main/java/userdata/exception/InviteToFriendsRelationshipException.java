package userdata.exception;

import userdata.data.FriendStatus;
import userdata.data.UsersRelationshipEntity;

public class InviteToFriendsRelationshipException extends RuntimeException {

    private final String currentUsername, partnerUsername;
    private final FriendStatus friendStatus;

    public InviteToFriendsRelationshipException(String currentUsername,
                                                String partnerUsername,
                                                FriendStatus friendStatus) {
        this.currentUsername = currentUsername;
        this.partnerUsername = partnerUsername;
        this.friendStatus = friendStatus;
    }

    public InviteToFriendsRelationshipException(UsersRelationshipEntity relationship) {
        this(relationship.getUser().getUsername(), relationship.getFriend().getUsername(), relationship.getRelationship());
    }

    @Override
    public String getMessage() {
        return String.format("Error during inviting to friends. User '%s' already has relationship '%s' with user '%s'",
                currentUsername, friendStatus, partnerUsername);
    }

}
