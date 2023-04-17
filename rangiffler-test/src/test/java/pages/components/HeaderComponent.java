package pages.components;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent extends BaseComponent<HeaderComponent> {

    @Step("Verify that title is [Rangiffler]")
    public HeaderComponent verifyTitleIsRangiffler() {
        $("h1").shouldHave(text("Rangiffler"));
        return this;
    }

    @Step("Click on [Log out] icon")
    public void clickLogoutIcon() {
        $("[data-testid=LogoutIcon]").click();
    }

}
