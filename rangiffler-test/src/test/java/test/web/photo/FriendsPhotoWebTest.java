package test.web.photo;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.*;
import model.CountryEnum;
import model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sashkir7.grpc.Photo;
import test.web.BaseWebTest;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static model.CountryEnum.*;
import static model.PartnerStatus.*;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.PHOTO)       @Tag(AllureTag.PHOTO)
@Story("Friends photos")
class FriendsPhotoWebTest extends BaseWebTest {

    @BeforeEach
    void openFriendsTravelsSection() {
        travelsPage.openFriendsTravelsSection();
    }

    @Test
    @DisplayName("User friends photos")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = RUSSIA), partners = {
            @WithPartner(status = NOT_FRIEND, photos = @WithPhoto),
            @WithPartner(status = INVITATION_SENT, photos = @WithPhoto),
            @WithPartner(status = INVITATION_RECEIVED, photos = @WithPhoto),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/cat.jpeg")),
            @WithPartner(status = FRIEND, photos = {
                    @WithPhoto(country = AUSTRALIA, imageClasspath = "img/tiger.jpeg"),
                    @WithPhoto(country = UKRAINE, imageClasspath = "img/svg.svg")}),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/leopard.jpeg"))}))
    void getFriendsPhotosTest(@Inject UserModel user) {
        List<Photo> photos = getUserFriendsPhotos(user);
        step("Verify all friends photos", () -> {
            travelsPage.verifyPhotosCount(photos.size())
                    .verifyCountyIsShadeOnWorldMap(CANADA, AUSTRALIA, UKRAINE)
                    .verifyCountryIsNotShadeOnWorldMap(RUSSIA);
            photos.forEach(travelsPage::verifyPhotoInformation);
        });
    }

    @Test
    @DisplayName("By country (photos exist)")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = RUSSIA), partners = {
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/cat.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/leopard.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = AUSTRALIA, imageClasspath = "img/tiger.jpeg"))}))
    void byCountryPhotosExistTest(@Inject UserModel user) {
        step("Select one country in world map", () ->
                travelsPage.clickToCountryOnWorldMap(CANADA));
        step("Verify friends photos by country", () -> {
            List<Photo> photos = getUserFriendsPhotosByCountry(user, CANADA);
            travelsPage.verifyPhotosCount(photos.size());
            photos.forEach(travelsPage::verifyPhotoInformation);
        });
    }

    @Test
    @DisplayName("By country (photos not exist)")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = INDIA), partners = {
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/cat.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/leopard.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = AUSTRALIA, imageClasspath = "img/tiger.jpeg"))}))
    void byCountryPhotosNotExistPhotoTest() {
        step("Select one country in world map", () ->
                travelsPage.clickToCountryOnWorldMap(INDIA));
        step("Verify friends photos by country", () ->
                travelsPage.verifyPhotosCount(0));
    }

    private List<Photo> getUserFriendsPhotos(UserModel user) {
        return user.getFriends().stream()
                .flatMap(friend -> friend.getPhotos().stream())
                .toList();
    }

    private List<Photo> getUserFriendsPhotosByCountry(UserModel user, CountryEnum country) {
        return getUserFriendsPhotos(user).stream()
                .filter(photo -> photo.getCountry().getCode().equals(country.getCode()))
                .toList();
    }

}
