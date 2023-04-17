package pages;

import com.codeborne.selenide.Selenide;
import config.AppProperties;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;

public class LandingPage extends BasePage<LandingPage> {

    @Step("Open landing page")
    public LandingPage open() {
        Selenide.open(AppProperties.APP_BASE_URL + "/landing");
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
