package pages;

import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import model.CountryEnum;
import pages.components.HeaderComponent;
import pages.components.UploadPhotoComponent;
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

public class YourTravelsPage extends BasePage<YourTravelsPage> {

    private final HeaderComponent header = new HeaderComponent();
    private final UploadPhotoComponent photoComponent = new UploadPhotoComponent();
    private final SelenideElement map = $("figure.worldmap__figure-container");

    @Step("Verify that your travels page is loaded")
    public YourTravelsPage verifyPageIsLoaded() {
        header.verifyTitleIsRangiffler();
        return this;
    }

    @Step("Open [People around] section")
    public void openPeopleAroundSection() {
        $(byTagAndText("button", "People Around")).click();
        $$("[id*=P-all] .MuiTableRow-root")
                .shouldHave(sizeGreaterThan(1), Duration.ofSeconds(20));
        // ToDo Немного подождать, чтобы нажатия на кнопки корректно обрабатывались:
        //  в идеале найти атрибут, за который можно "прицепиться"
        sleep(500);
    }

    @Step("Verify that country is shade on world map")
    public YourTravelsPage verifyCountyIsShadeOnWorldMap(CountryEnum country) {
        SelenideElement countryWebElement = getCountryWebElement(country).shouldBe(visible);
        assertTrue(getFillOpacityValueFromCountry(countryWebElement) > 0.2);
        return this;
    }

    @Step("Verify that country is not shade on world map")
    public YourTravelsPage verifyCountryIsNotShadeOnWorldMap(CountryEnum country) {
        SelenideElement countryWebElement = getCountryWebElement(country).shouldBe(visible);
        assertEquals(0.0, getFillOpacityValueFromCountry(countryWebElement));
        return this;
    }

    @Step("Verify user photo")
    public void verifyPhoto(Photo expectedPhoto) {
        SelenideElement actualPhoto = getPhotoWebElement(expectedPhoto.getId());
        step("Verify image", () ->
                actualPhoto.shouldHave(photo(expectedPhoto)));
        step("Verify country", () ->
                actualPhoto.closest("li")
                        .shouldHave(text(expectedPhoto.getCountry().getName())));
        step("Verify description", () ->
                actualPhoto.shouldHave(attribute("alt", expectedPhoto.getDescription())));
    }

    @Step("Verify photos count")
    public void verifyPhotosCount(int expectedCount) {
        $$("img.photo__list-item").shouldHave(CollectionCondition.size(expectedCount));
    }

    @Step("Click to country {county} on world map")
    public YourTravelsPage clickToCountryOnWorldMap(CountryEnum country) {
        getCountryWebElement(country).click();
        return this;
    }

    @Step("Verify do not contains photo in photos list")
    public void verifyDoNotContainsPhoto(String photoId) {
        getPhotoWebElement(photoId).shouldNotBe(visible);
    }

    @Step("Edit photo")
    public YourTravelsPage editPhoto(String photoId, CountryEnum country, String description) {
        getPhotoWebElement(photoId).click();
        $("[data-testid=EditIcon]").click();
        photoComponent.setCountry(country)
                .setDescription(description)
                .clickSaveButton()
                .verifyUploadPhotoModalWindowIsClosed();
        return this;
    }

    @Step("Delete photo")
    public YourTravelsPage deletePhoto(String photoId) {
        getPhotoWebElement(photoId).click();
        $("[data-testid=DeleteOutlineIcon]").click();
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

}
