package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class SuccessRegistrationPage extends BasePage<SuccessRegistrationPage> {

    @Step("Click on [Sign In] button in SuccessRegistrationPage")
    public void clickSignInButton() {
        $("a").shouldHave(text("Sign in!")).click();
    }

    @Step("Verify successful registration message in SuccessRegistrationPage")
    public SuccessRegistrationPage verifySuccessfulRegistrationMessage() {
        $(".form__paragraph").shouldHave(text("Congratulations! You've registered!"));
        return this;
    }

}
