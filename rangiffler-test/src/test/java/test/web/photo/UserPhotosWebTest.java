package test.web.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.Inject;
import jupiter.annotation.WithPhoto;
import model.CountryEnum;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sashkir7.grpc.Photo;
import test.web.BaseWebTest;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static model.CountryEnum.*;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
@Story("User photos")
class UserPhotosWebTest extends BaseWebTest {

    @Test
    @DisplayName("Get user photos")
    @ApiLogin(user = @GenerateUser(photos = {
            @WithPhoto(country = RUSSIA, imageClasspath = "img/cat.jpeg"),
            @WithPhoto(country = RUSSIA, imageClasspath = "img/leopard.jpeg"),
            @WithPhoto(country = AUSTRALIA, imageClasspath = "img/tiger.jpeg")}))
    void getUserPhotosTest(@Inject UserModel user) {
        step("Verify information in header", () ->
                headerComponent.verifyPhotosCount(user.getPhotos().size())
                        .verifyCountriesCount(2));
        step("Verify information in world map", () -> {
            travelsPage.verifyPhotosCount(user.getPhotos().size());
            for (Photo photo : user.getPhotos()) {
                CountryEnum country = CountryEnum.fromCode(photo.getCountry().getCode());
                travelsPage.verifyCountyIsShadeOnWorldMap(country)
                        .verifyPhotoInformation(photo);
            }
        });
    }

    @Test
    @DisplayName("Get user photos by country")
    @ApiLogin(user = @GenerateUser(photos = {
            @WithPhoto(country = UKRAINE, imageClasspath = "img/cat.jpeg"),
            @WithPhoto(country = UKRAINE, imageClasspath = "img/leopard.jpeg"),
            @WithPhoto(country = AUSTRALIA, imageClasspath = "img/png.png"),
            @WithPhoto(country = RUSSIA, imageClasspath = "img/tiger.jpeg")}))
    void getUserPhotosByCountry(@Inject UserModel user) {
        step("Verify information in header", () ->
                headerComponent.verifyPhotosCount(user.getPhotos().size())
                        .verifyCountriesCount(3));
        step("Select one country in world map", () ->
                travelsPage.clickToCountryOnWorldMap(UKRAINE));
        step("Verify information in world map", () -> {
            List<Photo> photos = getUserPhotosByCountry(user, UKRAINE);
            travelsPage.verifyPhotosCount(photos.size());
            photos.forEach(travelsPage::verifyPhotoInformation);
        });
    }

    @Test
    @DisplayName("Not found photos in country")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = RUSSIA)))
    void notFoundPhotosInCountryTest() {
        step("Select one country in world map", () ->
                travelsPage.clickToCountryOnWorldMap(AUSTRALIA));
        step("Verify that photos not found", () ->
                travelsPage.verifyPhotosCount(0));
    }

    private List<Photo> getUserPhotosByCountry(UserModel user, CountryEnum country) {
        return user.getPhotos().stream()
                .filter(photo -> photo.getCountry().getCode().equals(country.getCode()))
                .toList();
    }

}
