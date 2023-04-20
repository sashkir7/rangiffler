package test;

import api.GeoGrpcApi;
import api.PhotoGrpcApi;
import api.UserdataGrpcApi;

public abstract class BaseTest {

    protected UserdataGrpcApi userdataApi = new UserdataGrpcApi();
    protected PhotoGrpcApi photoApi = new PhotoGrpcApi();
    protected GeoGrpcApi geoApi = new GeoGrpcApi();

}
