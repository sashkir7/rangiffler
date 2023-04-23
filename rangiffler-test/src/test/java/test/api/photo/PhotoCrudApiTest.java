package test.api.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import data.repository.hibernate.HibernatePhotoRepository;
import data.repository.PhotoRepository;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import jupiter.annotation.*;
import model.CountryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sashkir7.grpc.Photo;
import test.api.BaseApiTest;

import java.util.*;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static model.CountryEnum.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic(AllureEpic.API)               @Tag(AllureTag.API)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
@Story("CRUD operations")
class PhotoCrudApiTest extends BaseApiTest {

    static Stream<Arguments> addPhotoTest() {
        return Stream.of(
                Arguments.of("jpeg.jpeg", RUSSIA, "Попутчица"),
                Arguments.of("jpg.jpg", RUSSIA, "Сходили на новый фильм"),
                Arguments.of("png.png", KAZAKHSTAN, "Собака товарища"),
                Arguments.of("svg.svg", KAZAKHSTAN, "")
        );
    }

    @MethodSource
    @ParameterizedTest(name = "{0}")
    @DisplayName("Add photo")
    void addPhotoTest(String image, CountryEnum country, String description) {
        Photo photo = getPhoto(image, country, description);
        verifyPhoto(photo, photoApi.addPhoto(photo));
    }

    @Test
    @DisplayName("Edit photo")
    void editPhotoTest(@WithPhoto Photo photo) {
        Photo modifierPhoto = photo.toBuilder()
                .setDescription(DataHelper.randomLorem())
                .setCountry(geoApi.getCountryByCode(BAHAMAS.getCode()))
                .build();
        verifyPhoto(modifierPhoto, photoApi.editPhoto(modifierPhoto));
    }

    @Test
    @DisplayName("Delete photo")
    void deletePhoto() {
        Photo photo = step("Create photo", () ->
                photoApi.addPhoto(getPhoto("png.png", ROMANIA, "Фоточка")));
        photoApi.deletePhoto(photo.getId());

        step("Verify that photo has been deleted", () -> {
            PhotoRepository repository = new HibernatePhotoRepository();
            assertNull(repository.findById(UUID.fromString(photo.getId())));
        });
    }

    private Photo getPhoto(String image, CountryEnum country, String description) {
        return Photo.newBuilder()
                .setUsername(DataHelper.randomUsername())
                .setDescription(description)
                .setPhoto(DataHelper.imageByClasspath("img/" + image))
                .setCountry(geoApi.getCountryByCode(country.getCode()))
                .build();
    }

    @Step("Verify photo")
    private void verifyPhoto(Photo expected, Photo actual) {
        if ("".equals(expected.getId())) {
            step("Verify id from new user", () ->
                    assertNotEquals("", actual.getId()));
        } else {
            step("Verify id from exists user", () ->
                    assertEquals(expected.getId(), actual.getId()));
        }

        step("Verify username", () ->
                assertEquals(expected.getUsername(), actual.getUsername()));
        step("Verify description", () ->
                assertEquals(expected.getDescription(), actual.getDescription()));
        step("Verify photo", () ->
                assertEquals(expected.getPhoto(), actual.getPhoto()));
        step("Verify country", () ->
                assertEquals(expected.getCountry(), actual.getCountry()));
    }

}
