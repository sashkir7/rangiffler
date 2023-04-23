package test.web.auth;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import data.repository.hibernate.HibernateUserdataRepository;
import data.repository.UserdataRepository;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import model.pages.RegisterPageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.SuccessRegistrationPage;
import test.web.BaseWebTest;

import static com.codeborne.selenide.Selenide.sleep;
import static io.qameta.allure.Allure.step;

@Epic(AllureEpic.WEB)           @Tag(AllureTag.WEB)
@Feature(AllureFeature.AUTH)    @Tag(AllureTag.AUTH)
@Story("Registration")
class RegistrationWebTest extends BaseWebTest {

    private final SuccessRegistrationPage successRegistrationPage = new SuccessRegistrationPage();
    private final String password = DataHelper.randomPassword();

    @BeforeEach
    void goToRegistrationPage() {
        landingPage.open().clickRegisterButton();
    }

    @Test
    @DisplayName("Success register")
    void successRegisterTest() {
        RegisterPageViewModel viewModel = getViewModel(DataHelper.randomUsername(), password, password);
        step("Register user", () ->
                registrationPage.fillForm(viewModel)
                        .clickSignUpButton());
        step("Verify that user success register", () -> {
            successRegistrationPage.verifySuccessfulRegistrationMessage()
                    .clickSignInButton();
            loginPage.verifyPageIsLoaded();
        });
        step("Verify that user has been added to userdata db", () ->
                verifyUserHasBeenAddedToUserdataDatabase(viewModel.getUsername()));
    }

    @Test
    @DisplayName("Already have account")
    void alreadyHaveAccountTest() {
        registrationPage.clickSignInButton();
        loginPage.verifyPageIsLoaded();
    }

    @Test
    @DisplayName("Username can not be empty")
    void usernameCanNotBeEmptyTest() {
        step("Register user", () ->
                registrationPage.fillForm(getViewModel(null, password, password))
                        .clickSignUpButton());
        step("Verify failure user registrations",
                registrationPage::verifyUsernameCanNotBeEmptyErrorMessage);
    }

    @Test
    @DisplayName("Password can not be empty")
    void passwordCanNotBeEmptyTest() {
        step("Register user", () ->
                registrationPage.fillForm(
                        getViewModel(DataHelper.randomUsername(), null, password)
                ).clickSignUpButton());
        step("Verify failure user registrations",
                registrationPage::verifyPasswordCanNotBeEmptyErrorMessage);
    }

    @Test
    @DisplayName("Passwords should be equal")
    void passwordShouldBeEqualTest() {
        step("Register user", () ->
                registrationPage.fillForm(
                        getViewModel(DataHelper.randomUsername(), password, DataHelper.randomPassword())
                ).clickSignUpButton());
        step("Verify failure user registrations",
                registrationPage::verifyPasswordsShouldBeEqualErrorMessage);
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

    private void verifyUserHasBeenAddedToUserdataDatabase(String username) {
        UserdataRepository repository = new HibernateUserdataRepository();
        for (int i = 1; i <= 30; i++) {
            if (repository.findByUsername(username) != null)
                break;
            sleep(50);
        }
    }

}
