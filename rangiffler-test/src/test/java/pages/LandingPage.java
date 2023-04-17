package pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;

public class LandingPage extends BasePage<LandingPage> {

    @Step("Open landing page")
    public LandingPage open() {
        Selenide.open("http://127.0.0.1:3001/landing");
        return this;
    }

    @Step("Verify that landing page is loaded")
    public LandingPage verifyPageIsLoaded() {
        $("h1").shouldHave(text("Be like Rangiffler"));
        return this;
    }

    @Step("Click on [Login] button")
    public void clickLoginButton() {
        $(byTagAndText("a", "Login")).click();
    }

    @Step("Click on [Register] button")
    public void clickRegisterButton() {
        $(byTagAndText("a", "Register")).click();
    }

}
