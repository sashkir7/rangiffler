package test.web.userdata;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.Inject;
import model.UserModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.components.ProfileComponent;
import sashkir7.grpc.User;
import test.web.BaseWebTest;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.USERDATA)    @Tag(AllureTag.USERDATA)
@Story("User profile")
class ProfileWebTest extends BaseWebTest {

    private final ProfileComponent profileComponent = new ProfileComponent();

    @BeforeEach
    void openUserProfile() {
        headerComponent.openProfileModalWindow();
    }

    @Test
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Get current user profile")
    void getCurrentUserProfileTest(@Inject UserModel user) {
        step("Verify user information in header",
                headerComponent::verifyUserDoesNotHaveAvatarImage);
        step("Verify user information in profile", () ->
                profileComponent.verifyProfileInformation(user)
                        .verifyUserDoesNotHaveAvatarImage());
    }

    @Test
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Update user profile")
    void updateUserProfileTest(@Inject UserModel user) {
        String firstname = DataHelper.randomFirstname();
        String lastname = DataHelper.randomLastname();
        String avatarClasspath = "img/girl.jpeg";

        step("Update user profile", () ->
                profileComponent.setFirstname(firstname)
                        .setLastname(lastname)
                        .setAvatar(avatarClasspath)
                        .verifyAvatarImage(avatarClasspath)
                        .clickSaveButton()
                        .verifyProfileModalWindowIsClosed());
        step("Verify that user avatar in header", () ->
                headerComponent.verifyUserAvatar(avatarClasspath));
        step("Verify that user data has been updated (API)", () -> {
            String expectedAvatar = DataHelper.imageByClasspath(avatarClasspath);
            verifyUserDataHasBeenUpdated(user.getUsername(), firstname, lastname, expectedAvatar);
        });
    }

    static Stream<Arguments> validateInputTest() {
        return Stream.of(
                Arguments.of("", "This field is required!"),
                Arguments.of("qq", "Length of this field must be more than 2 characters!")
        );
    }

    @MethodSource("validateInputTest")
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Validate firstname input")
    @ParameterizedTest(name = "value is {0}")
    void validateFirstnameInputTest(String invalidFirstname, String expectedErrorMessage) {
        step("Set profile information", () ->
                profileComponent.setFirstname(invalidFirstname));
        step("Verify error message", () ->
                profileComponent.verifyFirstnameInputErrorMessage(expectedErrorMessage)
                        .verifySaveButtonIsDisabled());
    }

    @MethodSource("validateInputTest")
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Validate lastname input")
    @ParameterizedTest(name = "value is {0}")
    void validateLastnameInputTest(String invalidLastname, String expectedErrorMessage) {
        step("Set profile information", () ->
                profileComponent.setLastname(invalidLastname));
        step("Verify error message", () ->
                profileComponent.verifyLastnameInputErrorMessage(expectedErrorMessage)
                        .verifySaveButtonIsDisabled());
    }

    private void verifyUserDataHasBeenUpdated(String username,
                                              String expectedFirstname,
                                              String expectedLastname,
                                              String expectedAvatar) {
        User actualUser = userdataApi.getUserByUsername(username);
        step("Verify firstname", () ->
                assertEquals(expectedFirstname, actualUser.getFirstname()));
        step("Verify lastname", () ->
                assertEquals(expectedLastname, actualUser.getLastname()));
        step("Verify avatar", () ->
                assertEquals(expectedAvatar, actualUser.getAvatar()));
    }

}
