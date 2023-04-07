package geo.service;

import com.google.protobuf.Empty;
import geo.data.CountryEntity;
import geo.data.repository.CountryRepository;
import guru.qa.grpc.niffler.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@GrpcService
public class GeoGrpcService extends GeoServiceGrpc.GeoServiceImplBase {

    private final CountryRepository countryRepository;

    @Autowired
    public GeoGrpcService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getAllCountries(Empty request, StreamObserver<Countries> responseObserver) {
        responseObserver.onNext(convertToGrpcCountriesFromEntities(countryRepository.findAll()));
        responseObserver.onCompleted();
    }

    @Override
    public void getCountryByCode(CodeRequest request, StreamObserver<Country> responseObserver) {
        responseObserver.onNext(convertToGrpcCountryFromEntity(countryRepository.findByCode(request.getCode())));
        responseObserver.onCompleted();
    }

    private Country convertToGrpcCountryFromEntity(CountryEntity entity) {
        return Country.newBuilder()
                .setId(entity.getId().toString())
                .setCode(entity.getCode())
                .setName(entity.getName())
                .build();
    }

    private Countries convertToGrpcCountriesFromEntities(Collection<CountryEntity> entities) {
        return Countries.newBuilder()
                .addAllCountries(entities.stream()
                        .map(this::convertToGrpcCountryFromEntity)
                        .toList()
                ).build();
    }

}
