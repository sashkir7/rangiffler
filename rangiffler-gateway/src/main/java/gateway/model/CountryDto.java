package gateway.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryDto {

    private UUID id;
    private String code, name;

}
