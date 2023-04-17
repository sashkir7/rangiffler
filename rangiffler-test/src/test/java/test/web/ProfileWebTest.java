package test.web;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
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
        mainPage.open()
                .getHeader()
                .openProfileModalWindow();
    }

    @Test
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Get current user profile")
    void getCurrentUserProfileTest(@Inject UserModel user) {
        mainPage.getHeader().verifyUserDoesNotHaveAvatarImage();
        profileComponent.verifyValues(user)
                .verifyUserDoesNotHaveAvatarImage();
    }

    @Test
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Update user profile (without avatar)")
    void updateUserProfileWithoutAvatarTest(@Inject UserModel user) {
        String firstname = DataHelper.randomFirstname();
        String lastname = DataHelper.randomLastname();

        profileComponent.setFirstname(firstname)
                .setLastname(lastname)
                .clickSaveButton()
                .verifyProfileModalWindowIsClosed();
        verifyUserDataHasBeenUpdated(user.getUsername(), firstname, lastname, "");
    }

    @Test
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Update user profile (with avatar)")
    void updateUserProfileWithAvatarTest(@Inject UserModel user) {
        String firstname = DataHelper.randomFirstname();
        String lastname = DataHelper.randomLastname();
        String avatarClasspath = "img/jpeg.jpeg";

        profileComponent.setFirstname(firstname)
                .setLastname(lastname)
                .setAvatar(avatarClasspath)
                .verifyAvatarImage(avatarClasspath)
                .clickSaveButton()
                .verifyProfileModalWindowIsClosed();
        mainPage.getHeader().verifyUserAvatar(avatarClasspath);

        String expectedAvatar = DataHelper.imageByClasspath(avatarClasspath);
        verifyUserDataHasBeenUpdated(user.getUsername(), firstname, lastname, expectedAvatar);
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
        profileComponent.setFirstname(invalidFirstname)
                .verifyFirstnameInputErrorMessage(expectedErrorMessage)
                .verifySaveButtonIsDisabled();
    }

    @MethodSource("validateInputTest")
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Validate lastname input")
    @ParameterizedTest(name = "value is {0}")
    void validateLastnameInputTest(String invalidLastname, String expectedErrorMessage) {
        profileComponent.setLastname(invalidLastname)
                .verifyLastnameInputErrorMessage(expectedErrorMessage)
                .verifySaveButtonIsDisabled();
    }

    @Step("Verify that user data has been updated")
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
