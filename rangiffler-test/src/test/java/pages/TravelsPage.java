package pages;

import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import model.CountryEnum;
import pages.components.PhotoComponent;
import sashkir7.grpc.Photo;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pages.conditions.PhotoCondition.photo;

public class TravelsPage extends BasePage<TravelsPage> {

    private final PhotoComponent photoComponent = new PhotoComponent();
    private final SelenideElement map = $("figure.worldmap__figure-container");

    @Step("Open [Friends travels] section in TravelsPage")
    public void openFriendsTravelsSection() {
        getButtonByText("Friends travels").click();
    }

    @Step("Open [People around] section in TravelsPage")
    public void openPeopleAroundSection() {
        getButtonByText("People Around").click();
        $$("[id*=P-all] .MuiTableRow-root")
                .shouldHave(sizeGreaterThan(1), Duration.ofSeconds(20));
        sleep(500);
    }

    @Step("Verify that country is shade on world map")
    public TravelsPage verifyCountyIsShadeOnWorldMap(CountryEnum... countries) {
        for (CountryEnum country : countries) {
            SelenideElement countryWebElement = getCountryWebElement(country).shouldBe(visible);
            assertTrue(getFillOpacityValueFromCountry(countryWebElement) > 0.2);
        }
        return this;
    }

    @Step("Verify that country is not shade on world map")
    public TravelsPage verifyCountryIsNotShadeOnWorldMap(CountryEnum country) {
        SelenideElement countryWebElement = getCountryWebElement(country).shouldBe(visible);
        assertEquals(0.0, getFillOpacityValueFromCountry(countryWebElement));
        return this;
    }

    @Step("Verify photo information in TravelsPage")
    public void verifyPhotoInformation(Photo expectedPhoto) {
        SelenideElement actualPhoto = getPhotoWebElement(expectedPhoto.getId());
        step("Verify image", () ->
                actualPhoto.shouldHave(photo(expectedPhoto)));
        step("Verify country", () ->
                actualPhoto.closest("li")
                        .shouldHave(text(expectedPhoto.getCountry().getName())));
        step("Verify description", () ->
                actualPhoto.shouldHave(attribute("alt", expectedPhoto.getDescription())));
    }

    @Step("Verify photos count in TravelsPage")
    public TravelsPage verifyPhotosCount(int expectedCount) {
        $$("img.photo__list-item").shouldHave(CollectionCondition.size(expectedCount));
        return this;
    }

    @Step("Click to country {country} on world map")
    public void clickToCountryOnWorldMap(CountryEnum country) {
        getCountryWebElement(country).click();
    }

    @Step("Verify do not contains photo in photos list in TravelsPage")
    public void verifyDoNotContainsPhoto(String photoId) {
        getPhotoWebElement(photoId).shouldNotBe(visible);
    }

    @Step("Edit photo in TravelsPage")
    public TravelsPage editPhoto(String photoId, CountryEnum country, String description) {
        getPhotoWebElement(photoId).click();
        photoComponent.clickEditPhotoButton()
                .setCountry(country)
                .setDescription(description)
                .clickSaveButton()
                .verifyUploadPhotoModalWindowIsClosed();
        return this;
    }

    @Step("Delete photo in TravelsPage")
    public TravelsPage deletePhoto(String photoId) {
        getPhotoWebElement(photoId).click();
        photoComponent.clickDeleteButton();
        $("[type=submit]").click();
        photoComponent.verifyUploadPhotoModalWindowIsClosed();
        return this;
    }

    private SelenideElement getCountryWebElement(CountryEnum country) {
        return map.find(format("path[d^='%s']", country.getFirstCoordinate()));
    }

    private SelenideElement getPhotoWebElement(String photoId) {
        return $(format("img[data-testid='%s']", photoId));
    }

    private double getFillOpacityValueFromCountry(SelenideElement countryWebElement) {
        String attribute = countryWebElement.shouldHave(attribute("style"))
                .getAttribute("style");
        assert attribute != null;
        return Double.parseDouble(substringBetween(attribute, "fill-opacity: ", "; stroke:"));
    }

    private SelenideElement getButtonByText(String title) {
        return $(byTagAndText("button", title));
    }

}
