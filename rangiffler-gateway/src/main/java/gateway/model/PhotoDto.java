package gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhotoDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("country")
    private CountryDto countryDto;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

}
