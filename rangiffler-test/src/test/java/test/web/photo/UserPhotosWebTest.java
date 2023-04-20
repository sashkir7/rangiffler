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
        headerComponent.verifyPhotosCount(user.getPhotos().size())
                .verifyCountriesCount(2);

        yourTravelsPage.verifyPhotosCount(user.getPhotos().size());
        user.getPhotos().forEach(photo -> {
            yourTravelsPage.verifyCountyIsShadeOnWorldMap(CountryEnum.fromCode(photo.getCountry().getCode()))
                    .verifyPhoto(photo);
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
        headerComponent.verifyPhotosCount(user.getPhotos().size())
                .verifyCountriesCount(3);
        yourTravelsPage.clickToCountryOnWorldMap(UKRAINE);
        List<Photo> photos = user.getPhotos().stream()
                .filter(a -> a.getCountry().getCode().equals(UKRAINE.getCode())).toList();
        yourTravelsPage.verifyPhotosCount(photos.size());
        photos.forEach(yourTravelsPage::verifyPhoto);
    }

    @Test
    @DisplayName("Not found photos in country")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = RUSSIA)))
    void notFoundPhotosInCountryTest() {
        yourTravelsPage.clickToCountryOnWorldMap(AUSTRALIA);
        yourTravelsPage.verifyPhotosCount(0);
    }

}
