package gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJson {

    private UUID id;
    private String username, firstname, lastname, avatar;

}

