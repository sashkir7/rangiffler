package gateway.model;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import sashkir7.grpc.Countries;
import sashkir7.grpc.Country;

@Data
@Builder
public class CountryDto {

    public static CountryDto fromGrpc(Country country) {
        return CountryDto.builder()
                .id(UUID.fromString(country.getId()))
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    public static Set<CountryDto> fromGrpc(Countries countries) {
        return countries.getCountriesList().stream()
                .map(CountryDto::fromGrpc)
                .collect(Collectors.toSet());
    }

    private UUID id;
    private String code, name;

    public Country toGrpc() {
        return Country.newBuilder()
                .setId(id.toString())
                .setCode(code)
                .setName(name)
                .build();
    }

}
