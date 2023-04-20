package test.web.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import com.codeborne.selenide.Selenide;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.Inject;
import model.CountryEnum;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sashkir7.grpc.Photo;
import test.web.BaseWebTest;

import static model.CountryEnum.RUSSIA;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
@Story("Delete photo")
class DeletePhotoWebTest extends BaseWebTest {

    private final CountryEnum country = RUSSIA;

    @Test
    @DisplayName("Delete photo")
    @ApiLogin(user = @GenerateUser)
    void deletePhotoTest(@Inject UserModel user) {
        Photo photo = createPhoto(user.getUsername(), country);
        Selenide.refresh();
        yourTravelsPage.deletePhoto(photo.getId())
                .verifyCountryIsNotShadeOnWorldMap(country)
                .verifyDoNotContainsPhoto(photo.getId());

        headerComponent.verifyPhotosCount(0)
                .verifyCountriesCount(0);
    }

    private Photo createPhoto(String username, CountryEnum country) {
        Photo input = Photo.newBuilder()
                .setUsername(username)
                .setPhoto(DataHelper.imageByClasspath("img/cat.jpeg"))
                .setCountry(geoApi.getCountryByCode(country))
                .setDescription(DataHelper.randomLorem())
                .build();
        return photoApi.addPhoto(input);
    }

}
