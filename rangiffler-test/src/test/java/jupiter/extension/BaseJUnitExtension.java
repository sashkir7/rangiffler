package jupiter.extension;

import api.GeoGrpcApi;
import api.PhotoGrpcApi;
import api.UserdataGrpcApi;
import api.auth.AuthClient;
import helper.DataHelper;
import jupiter.annotation.WithPhoto;
import jupiter.annotation.WithUser;
import model.UserModel;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import sashkir7.grpc.Photo;

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

    protected Photo convertToPhoto(String username, WithPhoto annotation) {
        return Photo.newBuilder()
                .setUsername(username)
                .setDescription(annotation.description())
                .setPhoto(DataHelper.imageByClasspath(annotation.imageClasspath()))
                .setCountry(geoApi.getCountryByCode(annotation.country().getCode()))
                .build();
    }

    protected UserModel convertToUserModel(String username, String password, String firstname, String lastname) {
        UserModel.UserModelBuilder builder = UserModel.builder()
                .username("".equals(username) ? DataHelper.randomUsername() : username)
                .firstname("".equals(firstname) ? DataHelper.randomFirstname() : firstname)
                .lastname("".equals(lastname) ? DataHelper.randomLastname() : lastname);
        if (password != null) {
            builder.password("".equals(password) ? DataHelper.randomPassword() : password);
        }
        return builder.build();
    }

    protected UserModel convertToUserModel(WithUser annotation) {
        return convertToUserModel(annotation.username(), null, annotation.firstname(), annotation.lastname());
    }

    private String getUniqueTestId(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getSimpleName() + ":"
                + extensionContext.getRequiredTestMethod().getName();
    }

}
