package jupiter.extension;

import api.GeoGrpcApi;
import api.PhotoGrpcApi;
import api.UserdataGrpcApi;
import api.auth.AuthClient;
import helper.DataHelper;
import jupiter.annotation.WithUser;
import model.UserModel;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

abstract class BaseJUnitExtension {

    protected final AuthClient authApi = new AuthClient();
    protected final UserdataGrpcApi userdataApi = new UserdataGrpcApi();
    protected final PhotoGrpcApi photoApi = new PhotoGrpcApi();
    protected final GeoGrpcApi geoApi = new GeoGrpcApi();

    protected void putToStore(ExtensionContext extensionContext, Namespace namespace, Object object) {
        String uniqueTestId = getUniqueTestId(extensionContext);
        extensionContext.getStore(namespace).put(uniqueTestId, object);
    }

    protected <T> T getFromStore(ExtensionContext extensionContext, Namespace namespace, Class<T> objectClass) {
        String uniqueTestId = getUniqueTestId(extensionContext);
        return extensionContext.getStore(namespace).get(uniqueTestId, objectClass);
    }

    protected UserModel convertToUserModel(WithUser annotation) {
        return UserModel.builder()
                .username("".equals(annotation.username()) ? DataHelper.randomUsername() : annotation.username())
                .password("".equals(annotation.password()) ? DataHelper.randomPassword() : annotation.password())
                .firstname("".equals(annotation.firstname()) ? DataHelper.randomFirstname() : annotation.firstname())
                .lastname("".equals(annotation.lastname()) ? DataHelper.randomLastname() : annotation.lastname())
                .avatar("".equals(annotation.avatarClasspath()) ? "" : DataHelper.imageByClasspath(annotation.avatarClasspath()))
                .build();
    }

    private String getUniqueTestId(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getSimpleName() + ":"
                + extensionContext.getRequiredTestMethod().getName();
    }

}
