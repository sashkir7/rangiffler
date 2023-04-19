package pages;

import com.codeborne.selenide.Selenide;
import config.AppProperties;
import io.qameta.allure.Step;
import pages.components.HeaderComponent;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.*;

public class MainPage extends BasePage<MainPage> {

    private final HeaderComponent header = new HeaderComponent();

    @Step("Open main page")
    public MainPage open() {
        Selenide.open(AppProperties.APP_BASE_URL);
        return this;
    }

    @Step("Verify that main page is loaded")
    public MainPage verifyPageIsLoaded() {
        header.verifyTitleIsRangiffler();
        return this;
    }

    @Step("Main page: open [People around] section")
    public void openPeopleAroundSection() {
        $(byTagAndText("button", "People Around")).click();
        $$("[id*=P-all] .MuiTableRow-root").shouldHave(sizeGreaterThan(2));
        // ToDo Немного подождать, чтобы нажатия на кнопки корректно обрабатывались:
        //  в идеале найти атрибут, за который можно "прицепиться"
        sleep(500);
    }

}
