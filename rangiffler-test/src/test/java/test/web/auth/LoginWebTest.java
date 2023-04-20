package test.web.auth;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.Inject;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.web.BaseWebTest;

import java.util.stream.Stream;

@Epic(AllureEpic.WEB)           @Tag(AllureTag.WEB)
@Feature(AllureFeature.AUTH)    @Tag(AllureTag.AUTH)
@Story("Login")
class LoginWebTest extends BaseWebTest {

    @Test
    @GenerateUser
    @DisplayName("Success login")
    void successLoginTest(@Inject UserModel userModel) {
        landingPage.open().clickLoginButton();
        loginPage.fillForm(userModel.getUsername(), userModel.getPassword())
                .clickSignInButton();
        yourTravelsPage.verifyPageIsLoaded();
    }

    @Test
    @DisplayName("Have no account")
    void haveNotAccountTest() {
        loginPage.open().clickSignUpButton();
        registrationPage.verifyPageIsLoaded();
    }

    static Stream<Arguments> invalidLoginTest() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, DataHelper.randomPassword()),
                Arguments.of(DataHelper.randomUsername(), null),
                Arguments.of(DataHelper.randomUsername(), DataHelper.randomPassword())
        );
    }

    @MethodSource
    @DisplayName("Invalid login:")
    @ParameterizedTest(name = "username = {0}, password = {1}")
    void invalidLoginTest(String username, String password) {
        loginPage.open()
                .fillForm(username, password)
                .clickSignInButton()
                .verifyBadCredentialsErrorMessage();
    }

}
