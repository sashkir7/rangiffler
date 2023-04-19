package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import model.PartnerStatus;
import model.UserModel;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static pages.conditions.PhotoCondition.photo;

public class PeopleAroundPage extends BasePage<PeopleAroundPage> {

    @Step("Invite to friends {username}")
    public void inviteToFriends(String username) {
        getAddToFriendButton(username).click();
        verifyActionStatusIsInvitationSent(username);
    }

    @Step("Submit friend {username}")
    public void submitFriend(String username) {
        getSubmitFriendButton(username).click();
        getRemoveFriendButton(username).shouldBe(visible);
    }

    @Step("Decline friend {username}")
    public void declineFriend(String username) {
        getDeclineFriendButton(username).click();
        $(byTagAndText("button", "Decline")).click();
        getAddToFriendButton(username).shouldBe(visible);
    }

    @Step("Remove friend {username}")
    public void removeFriend(String username) {
        getRemoveFriendButton(username).click();
        $(byTagAndText("button", "Delete")).click();
        getAddToFriendButton(username).shouldBe(visible);
    }

    @Step("Verify partner {partner.username}")
    public void verifyPartnerInformation(UserModel partner, PartnerStatus status) {
        SelenideElement partnerRow = findRowByUsername(partner.getUsername());
        step("Verify username cell", () ->
                partnerRow.find("td", 1)
                        .shouldHave(text(partner.getUsername())));
        step("Verify name cell", () ->
                partnerRow.find("td", 2)
                        .shouldHave(text(partner.getFirstname()), text(partner.getLastname())));

        verifyAvatarCell(partnerRow, partner.getAvatarImageClasspath());
        verifyStatusCell(partner.getUsername(), status);
    }

    private SelenideElement findRowByUsername(String username) {
        return $(byTagAndText("td", username)).closest("tr");
    }

    @Step("Verify avatar cell")
    private void verifyAvatarCell(SelenideElement partnerRow, String avatarImageClasspath) {
        SelenideElement avatarCell = partnerRow.find("td");
        if (avatarImageClasspath != null && !avatarImageClasspath.isBlank()) {
            // Partner has avatar
            avatarCell.find("img").shouldHave(photo(avatarImageClasspath));
        } else {
            // Partner does not have avatar
            avatarCell.find("svg").shouldHave(
                    attribute("data-testid", "PersonIcon"));
        }
    }

    @Step("Verify status (user actions) cell")
    private void verifyStatusCell(String username, PartnerStatus status) {
        switch (status) {
            case NOT_FRIEND -> getAddToFriendButton(username).shouldBe(visible);
            case INVITATION_SENT -> verifyActionStatusIsInvitationSent(username);
            case INVITATION_RECEIVED -> {
                getSubmitFriendButton(username).shouldBe(visible);
                getDeclineFriendButton(username).shouldBe(visible);
            }
            default -> getRemoveFriendButton(username).shouldBe(visible);
        }
    }

    // ToDo может на aria-label сменить? Чет id тупые какие-то...
    private SelenideElement getAddToFriendButton(String username) {
        return findRowByUsername(username).find("[data-testid=PersonAddAlt1Icon]");
    }

    private SelenideElement getSubmitFriendButton(String username) {
        return findRowByUsername(username).find("[data-testid=HowToRegIcon]");
    }

    private SelenideElement getDeclineFriendButton(String username) {
        return findRowByUsername(username).find("[data-testid=PersonOffIcon]");
    }

    private SelenideElement getRemoveFriendButton(String username) {
        return findRowByUsername(username).find("[data-testid=PersonRemoveAlt1Icon]");
    }

    private SelenideElement verifyActionStatusIsInvitationSent(String username) {
        return findRowByUsername(username).find("td", 3)
                .shouldHave(text("Invitation sent"));
    }

}
