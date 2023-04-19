package test.api;

import api.GeoGrpcApi;
import api.PhotoGrpcApi;
import test.BaseTest;

public abstract class BaseApiTest extends BaseTest {

    protected PhotoGrpcApi photoApi = new PhotoGrpcApi();
    protected GeoGrpcApi geoApi = new GeoGrpcApi();

}
