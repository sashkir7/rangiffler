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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.web.BaseWebTest;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;

@Epic(AllureEpic.WEB)           @Tag(AllureTag.WEB)
@Feature(AllureFeature.AUTH)    @Tag(AllureTag.AUTH)
@Story("Login")
class LoginWebTest extends BaseWebTest {

    @BeforeEach
    void goToLoginPage() {
        landingPage.open().clickLoginButton();
    }

    @Test
    @GenerateUser
    @DisplayName("Success login")
    void successLoginTest(@Inject UserModel user) {
        step("Login to system", () ->
                loginPage.fillForm(user)
                        .clickSignInButton());
        step("Verify success login",
                headerComponent::verifyTitleIsRangiffler);
    }

    @Test
    @DisplayName("Have no account")
    void haveNotAccountTest() {
        loginPage.clickSignUpButton();
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
        step("Login to system", () ->
                loginPage.fillForm(username, password)
                        .clickSignInButton());
        step("Verify failure login",
                loginPage::verifyBadCredentialsErrorMessage);
    }

}
