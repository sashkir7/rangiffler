package userdata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true, allowGetters = true)
public class UserModel {

    private String username, firstname, lastname, avatar;

}
