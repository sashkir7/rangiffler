package userdata.exception;

import userdata.data.FriendStatus;
import userdata.data.UsersRelationshipEntity;

public class RelationshipUsersFoundException extends RuntimeException {

    private final String currentUsername, partnerUsername;
    private final FriendStatus friendStatus;

    public RelationshipUsersFoundException(String currentUsername,
                                           String partnerUsername,
                                           FriendStatus friendStatus) {
        this.currentUsername = currentUsername;
        this.partnerUsername = partnerUsername;
        this.friendStatus = friendStatus;
    }

    public RelationshipUsersFoundException(UsersRelationshipEntity entity) {
        this(entity.getUser().getUsername(), entity.getFriend().getUsername(), entity.getRelationship());
    }

    @Override
    public String getMessage() {
        return String.format("User '%s' found relationship '%s' with user '%s'",
                currentUsername, friendStatus, partnerUsername);
    }

}
