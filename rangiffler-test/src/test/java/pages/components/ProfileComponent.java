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

    @Step("Set firstname {firstname} in Profile")
    public ProfileComponent setFirstname(String firstname) {
        return setValue(firstnameInput, firstname);
    }

    @Step("Set lastname {lastname} in Profile")
    public ProfileComponent setLastname(String lastname) {
        return setValue(lastnameInput, lastname);
    }

    @Step("Set avatar in Profile")
    public ProfileComponent setAvatar(String imageClasspath) {
        self.find("input[type=file]").uploadFromClasspath(imageClasspath);
        return this;
    }

    @Step("Click on [Save] button in Profile")
    public ProfileComponent clickSaveButton() {
        saveButton.click();
        return this;
    }

    @Step("Verify profile information")
    public ProfileComponent verifyProfileInformation(String expectedUsername,
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

    public ProfileComponent verifyProfileInformation(UserModel user) {
        return verifyProfileInformation(user.getUsername(), user.getFirstname(), user.getLastname());
    }

    @Step("Verify that user does not have avatar image in Profile")
    public void verifyUserDoesNotHaveAvatarImage() {
        self.find("[data-testid=PersonIcon]").shouldBe(visible);
    }

    @Step("Verify avatar image in Profile")
    public ProfileComponent verifyAvatarImage(String avatarImageClasspath) {
        self.find("img").shouldHave(photo(avatarImageClasspath));
        return this;
    }

    @Step("Verify firstname input error message in Profile")
    public ProfileComponent verifyFirstnameInputErrorMessage(String expectedErrorMessage) {
        return verifyErrorMessage(firstnameInput, expectedErrorMessage);
    }

    @Step("Verify lastname input error message in Profile")
    public ProfileComponent verifyLastnameInputErrorMessage(String expectedErrorMessage) {
        return verifyErrorMessage(lastnameInput, expectedErrorMessage);
    }

    @Step("Verify that [Save] button is disabled in Profile")
    public void verifySaveButtonIsDisabled() {
        saveButton.shouldBe(disabled);
    }

    @Step("Verify that profile modal window is closed in Profile")
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
