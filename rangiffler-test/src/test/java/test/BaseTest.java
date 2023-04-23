package test;

import api.GeoGrpcApi;
import api.PhotoGrpcApi;
import api.UserdataGrpcApi;
import jupiter.extension.JpaExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JpaExtension.class)
public abstract class BaseTest {

    protected UserdataGrpcApi userdataApi = new UserdataGrpcApi();
    protected PhotoGrpcApi photoApi = new PhotoGrpcApi();
    protected GeoGrpcApi geoApi = new GeoGrpcApi();

}
