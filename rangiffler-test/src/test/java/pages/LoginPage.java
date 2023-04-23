package pages;

import com.codeborne.selenide.Selenide;
import config.AppProperties;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    @Step("Open login page")
    public LoginPage open() {
        Selenide.open(AppProperties.AUTH_BASE_URL + "/login");
        return this;
    }

    @Step("Fill login form")
    public LoginPage fillForm(String username, String password) {
        $("#username").setValue(username);
        $("#password").setValue(password);
        return this;
    }

    @Step("Click on [Sign In] button in LoginPage")
    public LoginPage clickSignInButton() {
        $("button[type=submit]").click();
        return this;
    }

    @Step("Click on [Sign Up] button in LoginPage")
    public void clickSignUpButton() {
        $("p.form__paragraph").shouldHave(text("Have no account?"))
                .find("a")
                .shouldHave(text("Sign up!")).click();
    }

    @Step("Verify that login page is loaded")
    public void verifyPageIsLoaded() {
        $("h1").shouldHave(text("Login to Rangiffler"));
        $("button[type=submit]").shouldHave(text("Sign In"));
    }

    @Step("Verify bad credentials error message in LoginPage")
    public void verifyBadCredentialsErrorMessage() {
        $("p.form__error").shouldHave(text("Bad credentials"));
    }

}
