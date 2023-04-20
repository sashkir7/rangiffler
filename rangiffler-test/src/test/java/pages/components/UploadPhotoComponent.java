package pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import model.CountryEnum;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class UploadPhotoComponent extends BaseComponent<UploadPhotoComponent> {

    private final SelenideElement self = $(".MuiBox-root .MuiPaper-elevation");

    @Step("Upload photo by classpath")
    public UploadPhotoComponent uploadPhoto(String photoClasspath) {
        self.find("input[type=file]").uploadFromClasspath(photoClasspath);
        return this;
    }

    @Step("Set country")
    public UploadPhotoComponent setCountry(CountryEnum country) {
        self.find("[aria-haspopup=listbox]").click();
        $$("ul[role=listbox] li").find(text(country.toString())).click();
        return this;
    }

    @Step("Set description")
    public UploadPhotoComponent setDescription(String description) {
        self.find("textarea").setValue(description);
        return this;
    }

    @Step("Click on [Save] button")
    public UploadPhotoComponent clickSaveButton() {
        self.find("[type=submit]").click();
        return this;
    }

    @Step("Verify that save button is disabled")
    public UploadPhotoComponent verifySaveButtonIsDisabled() {
        self.find("[type=submit]").shouldBe(disabled);
        return this;
    }

    @Step("Verify that upload photo modal windows is closed")
    public void verifyUploadPhotoModalWindowIsClosed() {
        self.shouldNotBe(visible);
    }

}
