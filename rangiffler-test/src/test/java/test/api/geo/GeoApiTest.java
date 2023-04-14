package test.api.geo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import data.HibernateCountryRepository;
import data.entity.CountryEntity;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.CountryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import sashkir7.grpc.Country;
import test.api.BaseApiTest;

import java.util.*;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Epic(AllureEpic.API)           @Tag(AllureTag.API)
@Feature(AllureFeature.GEO)     @Tag(AllureTag.GEO)
class GeoApiTest extends BaseApiTest {

    @DisplayName("Get country information")
    @ParameterizedTest(name = "{0}")
    @EnumSource(value = CountryEnum.class, names = {"RUSSIA", "COTED_IVOIRE", "BOSNIA_AND_HERZEGOVINA"})
    void getCountryByCodeTest(CountryEnum countryEnum) {
        Country country = geoApi.getCountryByCode(countryEnum.getCode());
        step("Verify response", () -> {
            step("Verify id", () ->
                    assertNotEquals("", country.getId()));
            step("Verify name", () ->
                    assertEquals(countryEnum.toString(), country.getName()));
            step("Verify code", () ->
                    assertEquals(countryEnum.getCode(), country.getCode()));
        });
    }

    @Test
    @DisplayName("Get all countries")
    void getAllCountriesTest() {
        List<Country> actualCountries = geoApi.getAllCountries().getCountriesList();
        List<Country> expectedCountries = new HibernateCountryRepository().getAllCountries().stream()
                .map(CountryEntity::toGrpc)
                .toList();

        step("Verify response", () ->
                assertThat(actualCountries).hasSameElementsAs(expectedCountries));
    }

}
