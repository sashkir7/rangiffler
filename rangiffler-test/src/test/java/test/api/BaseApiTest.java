package test.api;

import api.GeoGrpcApi;
import api.UserdataGrpcApi;
import test.BaseTest;

public abstract class BaseApiTest extends BaseTest {

    protected UserdataGrpcApi userdataApi = new UserdataGrpcApi();
    protected GeoGrpcApi geoApi = new GeoGrpcApi();

}
