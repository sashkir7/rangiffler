package auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualPasswords
public class RegistrationModel extends BaseModel {

    @NotNull(message = "Username can not be null")
    @NotEmpty(message = "Username can not be empty")
    @Size(max = 20, message = "Username can`t be longer than 20 characters")
    private String username;

    @NotNull(message = "First name can not be null")
    @NotEmpty(message = "First name can not be empty")
    @Size(max = 20, message = "First name can`t be longer than 20 characters")
    private String firstname;

    @NotNull(message = "Last name can not be null")
    @NotEmpty(message = "Last name can not be empty")
    @Size(max = 20, message = "Last name can`t be longer than 20 characters")
    private String lastname;

    @JsonIgnore
    @NotNull(message = "Password can not be null")
    @Size(min = 3, max = 12, message = "Allowed password length should be from 3 to 12 characters")
    private String password;

    @JsonIgnore
    @NotNull(message = "Password submit can not be null")
    private String passwordSubmit;

    @Override
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
