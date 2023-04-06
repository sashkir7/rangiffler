package userdata.exception;

import userdata.data.FriendStatus;

public class RelationshipUsersNotFoundException extends RuntimeException {

    private final String currentUsername, partnerUsername;
    private final FriendStatus friendStatus;

    public RelationshipUsersNotFoundException(String currentUsername,
                                              String partnerUsername,
                                              FriendStatus friendStatus) {
        this.currentUsername = currentUsername;
        this.partnerUsername = partnerUsername;
        this.friendStatus = friendStatus;
    }

    @Override
    public String getMessage() {
        return String.format("User '%s' not found relationship '%s' with user '%s'",
                currentUsername, friendStatus, partnerUsername);
    }

}
