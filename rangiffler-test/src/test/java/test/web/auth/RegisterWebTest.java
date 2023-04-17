package test.web.auth;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import data.HibernateUserdataRepository;
import data.repository.UserdataRepository;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import model.pages.RegisterPageViewModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.SuccessRegistrationPage;
import test.web.BaseWebTest;

import static com.codeborne.selenide.Selenide.sleep;

@Epic(AllureEpic.WEB)           @Tag(AllureTag.WEB)
@Feature(AllureFeature.AUTH)    @Tag(AllureTag.AUTH)
@Story("Registration page")
class RegisterWebTest extends BaseWebTest {

    private final SuccessRegistrationPage successRegistrationPage = new SuccessRegistrationPage();
    private final String password = DataHelper.randomPassword();

    @Test
    @DisplayName("Success register")
    void successRegisterTest() {
        RegisterPageViewModel viewModel = getViewModel(DataHelper.randomUsername(), password, password);

        landingPage.open().clickRegisterButton();
        registrationPage.fillForm(viewModel)
                .clickSignUpButton();
        successRegistrationPage.verifySuccessfulRegistrationMessage()
                .clickSignInButton();
        loginPage.verifyPageIsLoaded();

        verifyUserHasBeenAddedToUserdataDatabase(viewModel.getUsername());
    }

    @Test
    @DisplayName("Already have account")
    void alreadyHaveAccountTest() {
        registrationPage.open()
                .clickSignInButton();
        loginPage.verifyPageIsLoaded();
    }

    @Test
    @DisplayName("Username can not be empty")
    void usernameCanNotBeEmptyTest() {
        registrationPage.open().
                fillForm(getViewModel(null, password, password))
                .clickSignUpButton()
                .verifyUsernameCanNotBeEmptyErrorMessage();
    }

    @Test
    @DisplayName("Password can not be empty")
    void passwordCanNotBeEmptyTest() {
        registrationPage.open()
                .fillForm(getViewModel(DataHelper.randomUsername(), null, password))
                .clickSignUpButton()
                .verifyPasswordCanNotBeEmptyErrorMessage();
    }

    @Test
    @DisplayName("Passwords should be equal")
    void passwordShouldBeEqualTest() {
        registrationPage.open()
                .fillForm(getViewModel(DataHelper.randomUsername(), password, DataHelper.randomPassword()))
                .clickSignUpButton()
                .verifyPasswordsShouldBeEqualErrorMessage();
    }

    private RegisterPageViewModel getViewModel(String username, String password, String submitPassword) {
        return RegisterPageViewModel.builder()
                .username(username)
                .firstname(DataHelper.randomFirstname())
                .lastname(DataHelper.randomLastname())
                .password(password)
                .submitPassword(submitPassword)
                .build();
    }

    @Step("Verify that user has been added to userdata database")
    private void verifyUserHasBeenAddedToUserdataDatabase(String username) {
        UserdataRepository repository = new HibernateUserdataRepository();
        for (int i = 1; i <= 30; i++) {
            if (repository.findByUsername(username) != null)
                break;
            sleep(50);
        }
    }

}
