package pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import model.UserModel;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static pages.conditions.PhotoCondition.photo;

public class FriendsComponent extends BaseComponent<FriendsComponent> {

    private final SelenideElement self = $(".MuiCard-root");

    @Step("Verify friend information in FriendComponent")
    public void verifyFriendInformation(UserModel friend) {
        SelenideElement friendRow = findRowByUsername(friend.getUsername());
        verifyFriendAvatarCell(friendRow, friend.getAvatarImageClasspath());
        step("Verify username cell", () ->
                friendRow.find("td", 1)
                        .shouldHave(text(friend.getUsername())));
        step("Verify action button cell", () ->
                friendRow.find("td", 2)
                    .find("button[aria-label='Remove friend']").shouldBe(visible)
                    .find("svg[data-testid=PersonRemoveAlt1Icon").shouldBe(visible));
    }

    @Step("Remove friend {username} in FriendComponent")
    public void removeFriend(String username) {
        findRowByUsername(username).find("[data-testid=PersonRemoveAlt1Icon]").click();
        $(byTagAndText("button", "Delete")).click();
        self.shouldNotBe(visible);
    }

    @Step("Verify friend avatar cell")
    private void verifyFriendAvatarCell(SelenideElement friendRow, String avatarImageClasspath) {
        SelenideElement avatarCell = friendRow.find("td");
        if (avatarImageClasspath != null && !avatarImageClasspath.isBlank()) {
            // Friend has avatar
            avatarCell.find("img").shouldHave(photo(avatarImageClasspath));
        } else {
            // Friend does not have avatar
            avatarCell.find("svg").shouldHave(
                    attribute("data-testid", "PersonIcon"));
        }
    }

    private SelenideElement findRowByUsername(String username) {
        return self.$(byTagAndText("td", username)).closest("tr");
    }

}
