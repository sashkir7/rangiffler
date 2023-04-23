package pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import model.CountryEnum;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PhotoComponent extends BaseComponent<PhotoComponent> {

    private final SelenideElement self = $(".MuiBox-root .MuiPaper-elevation");

    @Step("Upload photo by classpath in PhotoComponent")
    public PhotoComponent uploadPhoto(String photoClasspath) {
        self.find("input[type=file]").uploadFromClasspath(photoClasspath);
        return this;
    }

    @Step("Set country {country} in PhotoComponent")
    public PhotoComponent setCountry(CountryEnum country) {
        self.find("[aria-haspopup=listbox]").click();
        $$("ul[role=listbox] li").find(text(country.toString())).click();
        return this;
    }

    @Step("Set description {description} in PhotoComponent")
    public PhotoComponent setDescription(String description) {
        self.find("textarea").setValue(description);
        return this;
    }

    @Step("Click on [Save] button in PhotoComponent")
    public PhotoComponent clickSaveButton() {
        self.find("[type=submit]").click();
        return this;
    }

    @Step("Verify that save button is disabled in PhotoComponent")
    public void verifySaveButtonIsDisabled() {
        self.find("[type=submit]").shouldBe(disabled);
    }

    @Step("Verify that upload photo modal windows is closed")
    public void verifyUploadPhotoModalWindowIsClosed() {
        self.shouldNotBe(visible);
    }

}
