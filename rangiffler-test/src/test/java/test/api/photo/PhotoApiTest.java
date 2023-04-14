package test.api.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import data.HibernatePhotoRepository;
import data.repository.PhotoRepository;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import jupiter.annotation.*;
import model.CountryEnum;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sashkir7.grpc.Photo;
import sashkir7.grpc.User;
import test.api.BaseApiTest;

import java.util.*;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static model.CountryEnum.*;
import static model.PartnerStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Epic(AllureEpic.API)               @Tag(AllureTag.API)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
class PhotoApiTest extends BaseApiTest {

    static Stream<Arguments> addPhotoTest() {
        return Stream.of(
                Arguments.of("jpeg.jpeg", RUSSIA, "Попутчица"),
                Arguments.of("jpg.jpg", RUSSIA, "Сходили на новый фильм"),
                Arguments.of("png.png", KAZAKHSTAN, "Собака товарища"),
                Arguments.of("svg.svg", KAZAKHSTAN, ""),
                Arguments.of("tiff.tiff", ANGOLA, "")
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
        PhotoRepository repository = new HibernatePhotoRepository();
        Photo photo = photoApi.addPhoto(getPhoto("png.png", ROMANIA, "Фоточка"));
        photoApi.deletePhoto(photo.getId());
        step("Verify that photo has been deleted", () ->
                assertNull(repository.findById(UUID.fromString(photo.getId()))));
    }

    @Test
    @DisplayName("Get user photos")
    @GenerateUser(photos = {
            @WithPhoto(description = "Фотка", country = CAMBODIA),
            @WithPhoto(imageClasspath = "img/jpg.jpg", country = KAZAKHSTAN),
            @WithPhoto(imageClasspath = "img/png.png", description = "Hello world", country = UNITED_ARAB_EMIRATES)
    }, partners = @WithPartner(photos = @WithPhoto(imageClasspath = "img/svg.svg")))
    void getUserPhotosTest(@Inject UserModel user) {
        List<Photo> actualPhotos = photoApi.getUserPhotos(user.getUsername()).getPhotosList();
        step("Verify response", () ->
                assertThat(actualPhotos).hasSameElementsAs(user.getPhotos()));
    }

    @Test
    @DisplayName("Get user photos (user has no photos)")
    void getUserPhotosUserHasNoPhotosTest(@WithUser User user) {
        List<Photo> actualPhotos = photoApi.getUserPhotos(user.getUsername()).getPhotosList();
        step("Verify response", () ->
                assertEquals(Collections.emptyList(), actualPhotos));
    }

    @Test
    @DisplayName("Get all friends photos")
    @GenerateUser(photos = {@WithPhoto, @WithPhoto},
            partners = {
                    @WithPartner(status = INVITATION_RECEIVED, photos = @WithPhoto),
                    @WithPartner(status = INVITATION_SENT, photos = @WithPhoto),
                    @WithPartner(status = NOT_FRIEND, photos = @WithPhoto),
                    @WithPartner(status = FRIEND, photos = {
                            @WithPhoto(description = "Смотри!", imageClasspath = "img/png.png", country = ANGOLA),
                            @WithPhoto(imageClasspath = "img/svg.svg", country = KAZAKHSTAN)}),
                    @WithPartner(status = FRIEND, photos = @WithPhoto)})
    void getAllFriendsPhotosTest(@Inject UserModel user) {
        List<Photo> actualFriendsPhotos = photoApi.getAllFriendsPhotos(user.getUsername()).getPhotosList();
        List<Photo> expectedFriendsPhotos = user.getPartners().get(FRIEND).stream()
                .map(UserModel::getPhotos)
                .flatMap(Collection::stream)
                .toList();
        step("Verify response", () ->
                assertThat(actualFriendsPhotos).hasSameElementsAs(expectedFriendsPhotos));
    }

    @Test
    @DisplayName("Get all friends photos (friends have no photos)")
    @GenerateUser(photos = @WithPhoto, partners = {@WithPartner, @WithPartner})
    void getAllFriendsPhotosFriendsHaveNoPhotosTest(@Inject UserModel user) {
        List<Photo> actualFriendsPhotos = photoApi.getAllFriendsPhotos(user.getUsername()).getPhotosList();
        step("Verify response", () ->
                assertEquals(Collections.emptyList(), actualFriendsPhotos));
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
