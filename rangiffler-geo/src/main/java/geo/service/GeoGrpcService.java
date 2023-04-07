package geo.service;

import com.google.protobuf.Empty;
import geo.data.CountryEntity;
import geo.data.repository.CountryRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import sashkir7.grpc.CodeRequest;
import sashkir7.grpc.Countries;
import sashkir7.grpc.Country;
import sashkir7.grpc.GeoServiceGrpc;

import java.util.Collection;
import java.util.stream.Collectors;

@GrpcService
public class GeoGrpcService extends GeoServiceGrpc.GeoServiceImplBase {

    private final CountryRepository countryRepository;

    @Autowired
    public GeoGrpcService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getAllCountries(Empty request, StreamObserver<Countries> responseObserver) {
        responseObserver.onNext(convertToCountries(countryRepository.findAll()));
        responseObserver.onCompleted();
    }

    @Override
    public void getCountryByCode(CodeRequest request, StreamObserver<Country> responseObserver) {
        responseObserver.onNext(countryRepository.findByCode(request.getCode()).toGrpc());
        responseObserver.onCompleted();
    }

    private Countries convertToCountries(Collection<CountryEntity> entities) {
        return Countries.newBuilder()
                .addAllCountries(entities.stream()
                        .map(CountryEntity::toGrpc)
                        .collect(Collectors.toSet())
                ).build();
    }

}
