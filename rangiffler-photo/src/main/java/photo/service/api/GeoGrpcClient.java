package photo.service.api;

import guru.qa.grpc.niffler.grpc.CodeRequest;
import guru.qa.grpc.niffler.grpc.Country;
import guru.qa.grpc.niffler.grpc.GeoServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GeoGrpcClient {

    @GrpcClient("grpcGeoClient")
    private GeoServiceGrpc.GeoServiceBlockingStub geoServiceBlockingStub;

    public Country getCountryByCode(String code) {
        CodeRequest request = CodeRequest.newBuilder().setCode(code).build();
        return geoServiceBlockingStub.getCountryByCode(request);
    }

}
