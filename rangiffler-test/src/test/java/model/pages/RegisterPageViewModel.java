package model.pages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterPageViewModel {

    private String username, firstname, lastname, password, submitPassword;

}
