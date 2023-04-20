package pages;

import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import model.CountryEnum;
import pages.components.HeaderComponent;
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
    public void verifyCountyIsShadeOnWorldMap(CountryEnum country) {
        SelenideElement countryWebElement = getCountryWebElement(country).shouldBe(visible);
        assertTrue(getFillOpacityValueFromCountry(countryWebElement) > 0.2);
    }

    @Step("Verify that country is not shade on world map")
    public void verifyCountryIsNotShadeOnWorldMap(CountryEnum country) {
        SelenideElement countryWebElement = getCountryWebElement(country).shouldBe(visible);
        assertEquals(0.0, getFillOpacityValueFromCountry(countryWebElement));
    }

    @Step("Verify user photo")
    public void verifyPhoto(Photo expectedPhoto) {
        SelenideElement actualImage = $(format("img[data-testid='%s']", expectedPhoto.getId()));
        step("Verify image", () ->
                actualImage.shouldHave(photo(expectedPhoto)));
        step("Verify country", () ->
                actualImage.closest("li")
                        .shouldHave(text(expectedPhoto.getCountry().getName())));
        step("Verify description", () ->
                actualImage.shouldHave(attribute("alt", expectedPhoto.getDescription())));
    }

    private SelenideElement getCountryWebElement(CountryEnum country) {
        return map.find(format("path[d^='%s']", country.getFirstCoordinate()));
    }

    private double getFillOpacityValueFromCountry(SelenideElement countryWebElement) {
        String attribute = countryWebElement.shouldHave(attribute("style"))
                .getAttribute("style");
        assert attribute != null;
        return Double.parseDouble(substringBetween(attribute, "fill-opacity: ", "; stroke:"));
    }

}
