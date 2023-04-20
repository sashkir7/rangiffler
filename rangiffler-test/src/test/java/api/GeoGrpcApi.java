package api;

import model.CountryEnum;
import sashkir7.grpc.CodeRequest;
import sashkir7.grpc.Countries;
import sashkir7.grpc.Country;
import sashkir7.grpc.GeoServiceGrpc;

public final class GeoGrpcApi extends BaseGrpcApi {

    private final GeoServiceGrpc.GeoServiceBlockingStub blockingStub;

    public GeoGrpcApi() {
        super("localhost", 9004);
        blockingStub = GeoServiceGrpc.newBlockingStub(channel);
    }

    public Countries getAllCountries() {
        return blockingStub.getAllCountries(defaultEmptyInstance);
    }

    public Country getCountryByCode(String code) {
        return blockingStub.getCountryByCode(getCodeRequest(code));
    }

    public Country getCountryByCode(CountryEnum country) {
        return getCountryByCode(country.getCode());
    }

    private CodeRequest getCodeRequest(String code) {
        return CodeRequest.newBuilder().setCode(code).build();
    }

}
