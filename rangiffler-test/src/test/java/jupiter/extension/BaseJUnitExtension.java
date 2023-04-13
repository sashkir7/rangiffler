package jupiter.extension;

import api.GeoGrpcApi;
import api.PhotoGrpcApi;
import api.UserdataGrpcApi;
import api.auth.AuthClient;
import org.junit.jupiter.api.extension.ExtensionContext;

abstract class BaseJUnitExtension {

    protected final AuthClient authApi = new AuthClient();
    protected final UserdataGrpcApi userdataApi = new UserdataGrpcApi();
    protected final PhotoGrpcApi photoApi = new PhotoGrpcApi();
    protected final GeoGrpcApi geoApi = new GeoGrpcApi();

    protected String getUniqueTestId(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getSimpleName() + ":"
                + extensionContext.getRequiredTestMethod().getName();
    }

}
