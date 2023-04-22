package gateway.service.api;

import com.google.protobuf.Empty;
import gateway.model.CountryDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import sashkir7.grpc.GeoServiceGrpc;

import java.util.List;

@Component
public class GeoGrpcClient {

    @GrpcClient("grpcGeoClient")
    private GeoServiceGrpc.GeoServiceBlockingStub geoServiceBlockingStub;

    public List<CountryDto> getAllCountries() {
        return CountryDto.fromGrpc(geoServiceBlockingStub.getAllCountries(Empty.getDefaultInstance()));
    }

}
