package test.web.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.*;
import model.CountryEnum;
import model.UserModel;
import org.junit.jupiter.api.*;
import sashkir7.grpc.Photo;
import test.web.BaseWebTest;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static model.CountryEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
@Story("Add photo")
class AddPhotoWebTest extends BaseWebTest {

    private final String description = DataHelper.randomLorem(),
            imageClasspath = "img/leopard.jpeg";

    @BeforeEach
    void openAddPhotoModalWindow() {
        headerComponent.openAddPhotoModalWindow();
    }

    @Test
    @ApiLogin(user = @GenerateUser)
    @DisplayName("With description | user's first photo")
    void withDescriptionTest(@Inject UserModel user) {
        CountryEnum country = AUSTRIA;
        step("Upload photo", () ->
                photoComponent.uploadPhoto(imageClasspath)
                        .setCountry(country)
                        .setDescription(description)
                        .clickSaveButton()
                        .verifyUploadPhotoModalWindowIsClosed());
        step("Verify that photo has been added (UI)", () -> {
            headerComponent.verifyPhotosCount(1)
                    .verifyCountriesCount(1);
            travelsPage.verifyCountyIsShadeOnWorldMap(country);
        });
        step("Verify that photo has been added (API)", () -> {
            List<Photo> photos = photoApi.getUserPhotos(user.getUsername()).getPhotosList();
            assertEquals(1, photos.size());
            photos.forEach(travelsPage::verifyPhotoInformation);
        });
    }

    @Test
    @DisplayName("Without description | user already has some photos")
    @ApiLogin(user = @GenerateUser(photos = {
            @WithPhoto(country = SAUDI_ARABIA),
            @WithPhoto(imageClasspath = "img/tiger.jpeg"),
            @WithPhoto(imageClasspath = "img/cat.jpeg")}))
    void withoutDescriptionTest(@Inject UserModel user) {
        CountryEnum country = INDIA;
        step("Upload photo", () ->
                photoComponent.uploadPhoto(imageClasspath)
                        .setCountry(country)
                        .clickSaveButton()
                        .verifyUploadPhotoModalWindowIsClosed());
        step("Verify that photo has been added (UI)", () -> {
            headerComponent.verifyPhotosCount(user.getPhotos().size() + 1)
                    .verifyCountriesCount(3);
            travelsPage.verifyCountyIsShadeOnWorldMap(country);
        });
        step("Verify that photo has been added (API)", () -> {
            List<Photo> photos = photoApi.getUserPhotos(user.getUsername()).getPhotosList();
            assertEquals(user.getPhotos().size() + 1, photos.size());
            photos.forEach(travelsPage::verifyPhotoInformation);
        });
    }

    @Test
    @DisplayName("Validate: disabled button [Save]")
    @ApiLogin(user = @GenerateUser)
    void validateDisabledButtonSaveTest() {
        photoComponent.setCountry(HUNGARY)
                .setDescription(description)
                .verifySaveButtonIsDisabled();
    }

}
