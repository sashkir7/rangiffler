package test.api.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.*;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sashkir7.grpc.Photo;
import sashkir7.grpc.User;
import test.api.BaseApiTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static model.CountryEnum.*;
import static model.PartnerStatus.*;
import static model.PartnerStatus.FRIEND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic(AllureEpic.API)               @Tag(AllureTag.API)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
@Story("Get some photos")
class GetSomePhotosApiTest extends BaseApiTest {

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

}
