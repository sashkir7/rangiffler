package userdata.exception;

public class RelationshipWithMyselfException extends RuntimeException {

    private final String username;

    public RelationshipWithMyselfException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return String.format("Relationship with myself '%s' is impossible", username);
    }

}
