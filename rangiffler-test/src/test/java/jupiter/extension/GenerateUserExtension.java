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

        // Create user
        UserModel generatedUser = register(convertToUserModel(annotation));
        for (WithPhoto photoAnnotation : annotation.photos()) {
            Photo photo = createPhoto(generatedUser.getUsername(), photoAnnotation);
            generatedUser.addPhoto(photo);
        }

        // Create partners
        for (WithPartner partnerAnnotation : annotation.partners()) {
            UserModel partner = createPartner(generatedUser, partnerAnnotation);
            generatedUser.addPartner(partnerAnnotation.status(), partner);
        }

        putToStore(context, NAMESPACE, generatedUser);
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

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UserModel userModel = getFromStore(context, NAMESPACE, UserModel.class);

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

    private UserModel register(UserModel user) throws IOException {
        authApi.register(user);

        // Wait until userdata service will create new user
        UserdataRepository repository = new HibernateUserdataRepository();
        for (int i = 1; i <= 30; i++) {
            if (repository.findByUsername(user.getUsername()) != null)
                break;
            sleep(50);
        }

        return user;
    }

    private UserModel convertToUserModel(GenerateUser annotation) {
        return convertToUserModel(
                annotation.username(),
                annotation.password(),
                annotation.firstname(),
                annotation.lastname());
    }

    private Photo createPhoto(String username, WithPhoto annotation) {
        Photo photo = convertToPhoto(username, annotation);
        return photoApi.addPhoto(photo);
    }

    private UserModel createPartner(UserModel generatedUser, WithPartner annotation) {
        UserModel partnerUser = convertToUserModel(annotation.user());
        userdataApi.addUser(partnerUser);
        switch (annotation.status()) {
            case INVITATION_SENT -> userdataApi.inviteToFriends(generatedUser, partnerUser);
            case INVITATION_RECEIVED -> userdataApi.inviteToFriends(partnerUser, generatedUser);
            case FRIEND -> {
                userdataApi.inviteToFriends(partnerUser, generatedUser);
                userdataApi.submitFriends(generatedUser, partnerUser);
            }
        }

        for (WithPhoto photoAnnotation : annotation.photos()) {
            partnerUser.addPhoto(createPhoto(partnerUser.getUsername(), photoAnnotation));
        }

        return partnerUser;
    }

    private void deleteUserPhotos(UserModel user) {
        user.getPhotos().forEach(photo -> photoApi.deletePhoto(photo.getId()));
    }

}
