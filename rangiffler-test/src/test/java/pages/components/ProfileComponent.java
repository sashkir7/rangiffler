package pages.components;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;
import model.UserModel;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.Keys.*;
import static pages.conditions.PhotoCondition.*;

public class ProfileComponent extends BaseComponent<ProfileComponent> {

    private final SelenideElement self = $(".MuiCard-root");
    private final SelenideElement firstnameInput = self.find("input[name=firstname]"),
            lastnameInput = self.find("input[name=lastname]"),
            saveButton = self.find("button[type=submit]");

    @Step("Profile: set firstname")
    public ProfileComponent setFirstname(String firstname) {
        return setValue(firstnameInput, firstname);
    }

    @Step("Profile: set lastname")
    public ProfileComponent setLastname(String lastname) {
        return setValue(lastnameInput, lastname);
    }

    @Step("Profile: set avatar")
    public ProfileComponent setAvatar(String imageClasspath) {
        self.find("input[type=file]").uploadFromClasspath(imageClasspath);
        return this;
    }

    @Step("Profile: click on [Save] button")
    public ProfileComponent clickSaveButton() {
        saveButton.click();
        return this;
    }

    @Step("Profile: verify values")
    public ProfileComponent verifyValues(String expectedUsername,
                                         String expectedFirstname,
                                         String expectedLastname) {
        step("Verify username", () ->
                self.find(".MuiTypography-root")
                        .shouldHave(text("Username: " + expectedUsername)));
        step("Verify firstname", () ->
                firstnameInput.shouldHave(value(expectedFirstname)));
        step("Verify lastname", () ->
                lastnameInput.shouldHave(value(expectedLastname)));

        return this;
    }

    public ProfileComponent verifyValues(UserModel user) {
        return verifyValues(user.getUsername(), user.getFirstname(), user.getLastname());
    }

    @Step("Profile: verify that user does not have avatar image")
    public void verifyUserDoesNotHaveAvatarImage() {
        self.find("[data-testid=PersonIcon]").shouldBe(visible);
    }

    @Step("Profile: verify avatar image")
    public ProfileComponent verifyAvatarImage(String avatarImageClasspath) {
        self.find("img").shouldHave(photo(avatarImageClasspath));
        return this;
    }

    @Step("Profile: verify firstname input error message")
    public ProfileComponent verifyFirstnameInputErrorMessage(String expectedErrorMessage) {
        return verifyErrorMessage(firstnameInput, expectedErrorMessage);
    }

    @Step("Profile: verify lastname input error message")
    public ProfileComponent verifyLastnameInputErrorMessage(String expectedErrorMessage) {
        return verifyErrorMessage(lastnameInput, expectedErrorMessage);
    }

    @Step("Profile: verify that [Save] button is disabled")
    public void verifySaveButtonIsDisabled() {
        saveButton.shouldBe(disabled);
    }

    @Step("Profile: verify that profile modal window is closed")
    public void verifyProfileModalWindowIsClosed() {
        self.shouldNotBe(visible);
    }

    private ProfileComponent setValue(SelenideElement input, String value) {
        Keys key = "Mac OS X".equals(System.getProperty("os.name")) ? COMMAND : CONTROL;
        input.sendKeys(key + "A");
        input.sendKeys(BACK_SPACE);
        input.setValue(value);
        return this;
    }

    private ProfileComponent verifyErrorMessage(SelenideElement element, String expectedErrorMessage) {
        element.closest(".MuiFormControl-root")
                .find("p.Mui-error")
                .shouldHave(text(expectedErrorMessage));
        return this;
    }

}
