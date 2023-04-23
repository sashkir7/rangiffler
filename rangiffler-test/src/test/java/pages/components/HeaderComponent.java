package pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.conditions.PhotoCondition;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent extends BaseComponent<HeaderComponent> {

    private final SelenideElement self = $("header");

    @Step("Verify that title is [Rangiffler] in Header")
    public HeaderComponent verifyTitleIsRangiffler() {
        self.find("h1").shouldHave(text("Rangiffler"));
        return this;
    }

    @Step("Click on [Log out] icon in Header")
    public void clickLogoutIcon() {
        self.find("[data-testid=LogoutIcon]").click();
    }

    @Step("Open [Profile] modal window")
    public void openProfileModalWindow() {
        self.find("[data-testid=PersonIcon]").click();
    }

    @Step("Open [Friends] modal window")
    public void openFriendsModalWindow() {
        self.find("[data-testid=GroupIcon]").click();
    }

    @Step("Open [Add photo] modal window")
    public void openAddPhotoModalWindow() {
        self.find("[data-testid=AddCircleOutlineIcon]").closest("button").click();
    }

    @Step("Verify that user does not have avatar image in Header")
    public HeaderComponent verifyUserDoesNotHaveAvatarImage() {
        self.find("[data-testid=PersonIcon]").shouldBe(visible);
        return this;
    }

    @Step("Verify user avatar in Header")
    public void verifyUserAvatar(String avatarClasspath) {
        self.find(".MuiAvatar-root img").shouldHave(PhotoCondition.photo(avatarClasspath));
    }

    @Step("Verify friends count in Header")
    public HeaderComponent verifyFriendsCount(int expectedFriendsCount) {
        self.find("[aria-label='Your friends']")
                .shouldHave(text(String.valueOf(expectedFriendsCount)));
        return this;
    }

    @Step("Verify photos count in Header")
    public HeaderComponent verifyPhotosCount(int expectedFriendsCount) {
        self.find("[aria-label='Your photos']")
                .shouldHave(text(String.valueOf(expectedFriendsCount)));
        return this;
    }

    @Step("Verify countries count in Header")
    public void verifyCountriesCount(int expectedCountriesCount) {
        self.find("[aria-label='Your visited countries']")
                .shouldHave(text(String.valueOf(expectedCountriesCount)));
    }

}
