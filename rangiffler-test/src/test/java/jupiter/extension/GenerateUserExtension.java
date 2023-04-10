package jupiter.extension;

import helper.DataHelper;
import jupiter.annotation.WithPartner;
import jupiter.annotation.WithPhoto;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.WithUser;
import model.UserModel;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import sashkir7.grpc.*;

import java.io.IOException;

public class GenerateUserExtension extends BaseJUnitExtension implements BeforeEachCallback, AfterEachCallback {

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

        context.getStore(NAMESPACE).put(getUniqueTestId(context), user);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        // ToDo тут удаление тестовых артефактов
        UserModel userModel = context.getStore(NAMESPACE).get(getUniqueTestId(context), UserModel.class);
        System.out.println(userModel);
    }

    private UserModel handleWithUserAnnotation(WithUser annotation) throws IOException {
        UserModel user = convertAnnotationToUserModel(annotation);
        authApi.register(user);

        for (WithPhoto photoAnnotation : annotation.photos()) {
            Photo input = getCountryAndConvertToPhoto(user.getUsername(), photoAnnotation);
            user.addPhoto(photoApi.addPhoto(input));
        }
        return user;
    }

    private UserModel convertAnnotationToUserModel(WithUser annotation) {
        return UserModel.builder()
                .username("".equals(annotation.username()) ? DataHelper.randomUsername() : annotation.username())
                .password("".equals(annotation.password()) ? DataHelper.randomPassword() : annotation.password())
                .firstname(DataHelper.randomFirstname())
                .lastname(DataHelper.randomLastname())
                .build();
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

}
