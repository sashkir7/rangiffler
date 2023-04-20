package test.web.photo;

import jupiter.annotation.*;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sashkir7.grpc.Photo;
import test.web.BaseWebTest;

import java.util.List;

import static model.CountryEnum.*;
import static model.PartnerStatus.*;

// ToDo refactoring
class FriendsPhotoWebTest extends BaseWebTest {

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

        List<Photo> photos = user.getFriends().stream()
                .flatMap(a -> a.getPhotos().stream())
                .toList();


        yourTravelsPage.openFriendsTravelsSection();
        yourTravelsPage.verifyPhotosCount(photos.size());
        yourTravelsPage.verifyCountyIsShadeOnWorldMap(CANADA)
                .verifyCountyIsShadeOnWorldMap(AUSTRALIA)
                .verifyCountyIsShadeOnWorldMap(UKRAINE);

        yourTravelsPage.verifyCountryIsNotShadeOnWorldMap(RUSSIA);

        photos.forEach(yourTravelsPage::verifyPhoto);

        yourTravelsPage.clickToCountryOnWorldMap(CANADA);

        List<Photo> photos1 = photos.stream().filter(f -> f.getCountry().getCode().equals(CANADA.getCode()))
                .toList();
        yourTravelsPage.verifyPhotosCount(photos1.size());
        photos1.forEach(a -> yourTravelsPage.verifyPhoto(a));
    }

    @Test
    @DisplayName("by country (extist photos)")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = RUSSIA), partners = {
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/cat.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/leopard.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = AUSTRALIA, imageClasspath = "img/tiger.jpeg"))}))
    void test123(@Inject UserModel user) {
        yourTravelsPage.openFriendsTravelsSection();
        yourTravelsPage.clickToCountryOnWorldMap(CANADA);

        List<Photo> photos1 = user.getFriends().stream().flatMap(a -> a.getPhotos().stream())
                .filter(f -> f.getCountry().getCode().equals(CANADA.getCode()))
                .toList();
        yourTravelsPage.verifyPhotosCount(photos1.size());
        photos1.forEach(a -> yourTravelsPage.verifyPhoto(a));
    }

    @Test
    @DisplayName("by country (not exist photos)")
    @ApiLogin(user = @GenerateUser(photos = @WithPhoto(country = INDIA), partners = {
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/cat.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = CANADA, imageClasspath = "img/leopard.jpeg")),
            @WithPartner(status = FRIEND, photos = @WithPhoto(country = AUSTRALIA, imageClasspath = "img/tiger.jpeg"))}))
    void test1234(@Inject UserModel user) {
        yourTravelsPage.openFriendsTravelsSection();
        yourTravelsPage.clickToCountryOnWorldMap(INDIA);

        yourTravelsPage.verifyPhotosCount(0);
    }

}
