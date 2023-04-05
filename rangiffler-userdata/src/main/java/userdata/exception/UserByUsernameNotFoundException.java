package userdata.exception;

import jakarta.persistence.PersistenceException;

import java.io.Serial;

public class UserByUsernameNotFoundException extends PersistenceException {

    @Serial
    private static final long serialVersionUID = 7505726390776409077L;

    private final String username;

    public UserByUsernameNotFoundException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return String.format("User with username '%s' not found", username);
    }

}
