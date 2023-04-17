package pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    @Step("Open main page")
    public MainPage open() {
        Selenide.open("http://127.0.0.1:3001/");
        return this;
    }

    @Step("Verify that main page is loaded")
    public MainPage verifyPageIsLoaded() {
        $("h1").shouldHave(text("Rangiffler"));
        $("[data-testid=PersonIcon]").shouldBe(visible);
        return this;
    }

}
