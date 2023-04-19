package pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.conditions.PhotoCondition;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent extends BaseComponent<HeaderComponent> {

    private final SelenideElement self = $("header");

    @Step("Header: verify that title is [Rangiffler]")
    public HeaderComponent verifyTitleIsRangiffler() {
        self.find("h1").shouldHave(text("Rangiffler"));
        return this;
    }

    @Step("Header: click on [Log out] icon")
    public void clickLogoutIcon() {
        self.find("[data-testid=LogoutIcon]").click();
    }

    @Step("Header: open [Profile] modal window")
    public void openProfileModalWindow() {
        self.find("[data-testid=PersonIcon]").click();
    }

    @Step("Header: open [Friends] modal window")
    public void openFriendsModalWindow() {
        self.find("[data-testid=GroupIcon]").click();
    }

    @Step("Header: verify that user does not have avatar image")
    public HeaderComponent verifyUserDoesNotHaveAvatarImage() {
        self.find("[data-testid=PersonIcon]").shouldBe(visible);
        return this;
    }

    @Step("Header: verify user avatar")
    public void verifyUserAvatar(String avatarClasspath) {
        self.find(".MuiAvatar-root img").shouldHave(PhotoCondition.photo(avatarClasspath));
    }

    @Step("Header: verify friends count")
    public HeaderComponent verifyFriendsCount(int expectedFriendsCount) {
        self.find("[aria-label='Your friends']")
                .shouldHave(text(String.valueOf(expectedFriendsCount)));
        return this;
    }

}
