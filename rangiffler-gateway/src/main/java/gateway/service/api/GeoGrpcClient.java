package gateway.service.api;

import com.google.protobuf.Empty;
import gateway.model.CountryDto;
import guru.qa.grpc.niffler.grpc.CodeRequest;
import guru.qa.grpc.niffler.grpc.Countries;
import guru.qa.grpc.niffler.grpc.Country;
import guru.qa.grpc.niffler.grpc.GeoServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GeoGrpcClient {

    @GrpcClient("grpcGeoClient")
    private GeoServiceGrpc.GeoServiceBlockingStub geoServiceBlockingStub;

    public Set<CountryDto> getAllCountries() {
        return convertToCountryDtos(geoServiceBlockingStub.getAllCountries(Empty.getDefaultInstance()));
    }

    public CountryDto getCountryByCode(String code) {
        CodeRequest request = CodeRequest.newBuilder().setCode(code).build();
        return convertToCountryDto(geoServiceBlockingStub.getCountryByCode(request));
    }

    private CountryDto convertToCountryDto(Country country) {
        return CountryDto.builder()
                .id(UUID.fromString(country.getId()))
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    private Set<CountryDto> convertToCountryDtos(Countries countries) {
        return countries.getCountriesList().stream()
                .map(this::convertToCountryDto)
                .collect(Collectors.toSet());
    }

}
