package jupiter.extension;

import data.HibernateUserdataRepository;
import data.repository.UserdataRepository;
import jupiter.annotation.*;
import model.UserModel;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import sashkir7.grpc.*;

import java.io.IOException;
import java.util.Collection;

import static com.codeborne.selenide.Selenide.sleep;

public class GenerateUserExtension extends BaseJUnitExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {

    public static final Namespace NAMESPACE = Namespace.create(GenerateUserExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateUser annotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        if (annotation == null)
            return;

        // Create user
        UserModel user = handleWithUserAnnotation(annotation.user());

        // Create partners
        for (WithPartner partnerAnnotation : annotation.partners()) {
            UserModel partner = handleWithUserAnnotation(partnerAnnotation.user());
            switch (partnerAnnotation.status()) {
                case INVITATION_SENT -> userdataApi.inviteToFriends(user, partner);
                case INVITATION_RECEIVED -> userdataApi.inviteToFriends(partner, user);
                case FRIEND -> {
                    userdataApi.inviteToFriends(partner, user);
                    userdataApi.submitFriends(user, partner);
                }
            }
            user.addPartner(partnerAnnotation.status(), partner);
        }

        putToStore(context, NAMESPACE, user);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UserModel userModel = getFromStore(context, NAMESPACE, UserModel.class);
        if (userModel == null)
            throw new RuntimeException("User not found in context store");

        // Delete user
        userdataApi.deleteUser(userModel.getUsername());
        deleteUserPhotos(userModel);

        // Delete partners
        userModel.getPartners().values().stream()
                .flatMap(Collection::stream)
                .peek(partner -> userdataApi.deleteUser(partner.getUsername()))
                .forEach(this::deleteUserPhotos);

        // ToDo Удалить через DAO auth
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserModel.class)
                && parameterContext.getParameter().isAnnotationPresent(Inject.class);
    }

    @Override
    public UserModel resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return getFromStore(extensionContext, NAMESPACE, UserModel.class);
    }

    private UserModel handleWithUserAnnotation(WithUser annotation) throws IOException {
        UserModel user = convertToUserModel(annotation);
        authApi.register(user);

        // Wait until userdata service will create new user
        UserdataRepository repository = new HibernateUserdataRepository();
        for (int i = 1; i <= 30; i++) {
            if (repository.findByUsername(user.getUsername()) != null)
                break;
            sleep(50);
        }

        for (WithPhoto photoAnnotation : annotation.photos()) {
            Photo input = getCountryAndConvertToPhoto(user.getUsername(), photoAnnotation);
            user.addPhoto(photoApi.addPhoto(input));
        }
        return user;
    }

    private Photo getCountryAndConvertToPhoto(String username, WithPhoto annotation) {
        Country country = geoApi.getCountryByCode(annotation.country());
        return Photo.newBuilder()
                .setUsername(username)
                .setDescription(annotation.description())
                .setPhoto(annotation.img())
                .setCountry(country)
                .build();
    }

    private void deleteUserPhotos(UserModel user) {
        user.getPhotos().forEach(photo -> photoApi.deletePhoto(photo.getId()));
    }

}
