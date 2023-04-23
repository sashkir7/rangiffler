package test.web.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import helper.DataHelper;
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

import static model.CountryEnum.NEW_ZEALAND;
import static model.CountryEnum.RUSSIA;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
@Story("Edit photo")
class EditPhotoWebTest extends BaseWebTest {

    private final CountryEnum country = NEW_ZEALAND;
    private final String description = DataHelper.randomLorem();

    @Test
    @DisplayName("Update photo information")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = RUSSIA)))
    void updatePhotoInformationTest(@Inject UserModel user) {
        Photo photo = getFirstUserPhoto(user.getUsername());
        travelsPage.editPhoto(photo.getId(), country, description)
                .verifyCountryIsNotShadeOnWorldMap(RUSSIA)
                .verifyCountyIsShadeOnWorldMap(country)
                .verifyPhotoInformation(updatePhotoInformation(photo, country, description));
    }

    private Photo getFirstUserPhoto(String username) {
        return photoApi.getUserPhotos(username).getPhotosList().stream().findFirst().orElseThrow();
    }

    private Photo updatePhotoInformation(Photo photo, CountryEnum country, String description) {
        return photo.toBuilder()
                .setCountry(geoApi.getCountryByCode(country.getCode()))
                .setDescription(description)
                .build();
    }

}
