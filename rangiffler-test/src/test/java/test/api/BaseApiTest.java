package test.api;

import api.GeoGrpcApi;
import api.PhotoGrpcApi;
import api.UserdataGrpcApi;
import test.BaseTest;

public abstract class BaseApiTest extends BaseTest {

    protected UserdataGrpcApi userdataApi = new UserdataGrpcApi();
    protected PhotoGrpcApi photoApi = new PhotoGrpcApi();
    protected GeoGrpcApi geoApi = new GeoGrpcApi();

}
