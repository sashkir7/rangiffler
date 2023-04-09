package api;

import sashkir7.grpc.CodeRequest;
import sashkir7.grpc.Countries;
import sashkir7.grpc.Country;
import sashkir7.grpc.GeoServiceGrpc;

public final class GeoGrpcApi extends BaseGrpcApi {

    private final GeoServiceGrpc.GeoServiceBlockingStub geoServiceBlockingStub;

    public GeoGrpcApi() {
        super("localhost", 9004);
        geoServiceBlockingStub = GeoServiceGrpc.newBlockingStub(channel);
    }

    public Countries getAllCountries() {
        return geoServiceBlockingStub.getAllCountries(defaultEmptyInstance);
    }

    public Country getCountryByCode(String code) {
        return geoServiceBlockingStub.getCountryByCode(getCodeRequest(code));
    }

    private CodeRequest getCodeRequest(String code) {
        return CodeRequest.newBuilder().setCode(code).build();
    }

}
