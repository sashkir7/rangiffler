package pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import lombok.Getter;
import pages.components.HeaderComponent;

public class MainPage extends BasePage<MainPage> {

    @Getter
    private final HeaderComponent header = new HeaderComponent();

    @Step("Open main page")
    public MainPage open() {
        Selenide.open("http://127.0.0.1:3001/");
        return this;
    }

    @Step("Verify that main page is loaded")
    public MainPage verifyPageIsLoaded() {
        header.verifyTitleIsRangiffler();
        return this;
    }

}
