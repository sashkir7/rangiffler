package pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    @Step("Open login page")
    public LoginPage open() {
        Selenide.open("http://127.0.0.1:9000/login");
        return this;
    }

    @Step("Verify that page is loaded")
    public LoginPage verifyPageIsLoaded() {
        $("h1").shouldHave(text("Login to Rangiffler"));
        $("button[type=submit]").shouldHave(text("Sign In"));
        return this;
    }

}
