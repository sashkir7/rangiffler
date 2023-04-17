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
        verifyStatusCell(partnerRow, status);
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
    private void verifyStatusCell(SelenideElement partnerRow, PartnerStatus status) {
        SelenideElement statusCell = partnerRow.find("td", 3);
        switch (status) {
            case NOT_FRIEND -> verifyStatusActionButton(
                    statusCell, "Add friend", "PersonAddAlt1Icon");
            case INVITATION_SENT -> statusCell.shouldHave(text("Invitation sent"));
            case INVITATION_RECEIVED -> {
                verifyStatusActionButton(statusCell, "Accept invitation", "HowToRegIcon");
                verifyStatusActionButton(statusCell, "Decline invitation", "PersonOffIcon");
            }
            default -> verifyStatusActionButton(
                    statusCell, "Remove friend", "PersonRemoveAlt1Icon");
        }
    }

    private void verifyStatusActionButton(SelenideElement statusCell,
                                          String ariaLabelAttrValue,
                                          String iconDataTestIdValue) {
        statusCell.find(String.format("button[aria-label='%s']", ariaLabelAttrValue)).shouldBe(visible)
                .find(String.format("svg[data-testid='%s']", iconDataTestIdValue)).shouldBe(visible);
    }

}
