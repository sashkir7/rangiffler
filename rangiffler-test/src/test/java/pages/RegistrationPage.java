package pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import model.pages.RegisterPageViewModel;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage extends BasePage<RegistrationPage> {

    @Step("Open registration page")
    public RegistrationPage open() {
        Selenide.open("http://127.0.0.1:9000/register");
        return this;
    }

    @Step("Fill registration form")
    public RegistrationPage fillForm(RegisterPageViewModel viewModel) {
        $("#username").setValue(viewModel.getUsername());
        $("#firstname").setValue(viewModel.getFirstname());
        $("#lastname").setValue(viewModel.getLastname());
        $("#password").setValue(viewModel.getPassword());
        $("#passwordSubmit").setValue(viewModel.getSubmitPassword());
        return this;
    }

    @Step("Click on [Sign up] button")
    public RegistrationPage clickSignUpButton() {
        $("button[type=submit]").click();
        return this;
    }

    @Step("Click on [Sign In] button")
    public void clickSignInButton() {
        $("p.form__paragraph").shouldHave(text("Already have an account?"))
                .find("a")
                .shouldHave(text("Sign in!")).click();
    }

    @Step("Verify username can not be empty error message")
    public void verifyUsernameCanNotBeEmptyErrorMessage() {
        $("#username").closest("label")
                .find("span.form__error")
                .shouldHave(text("Username can not be empty"));
    }

    @Step("Verify password can not be empty error message")
    public void verifyPasswordCanNotBeEmptyErrorMessage() {
        $("#password").closest("label").find("span.form__error")
                .shouldHave(text("Allowed password length should be from 3 to 12 characters"));
    }

    @Step("Verify passwords should be equal error message")
    public void verifyPasswordsShouldBeEqualErrorMessage() {
        $("#password").closest("label").find("span.form__error")
                .shouldHave(text("Passwords should be equal"));
    }

}
