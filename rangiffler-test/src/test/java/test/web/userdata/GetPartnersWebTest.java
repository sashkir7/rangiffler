package test.web.userdata;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.*;
import model.PartnerStatus;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.web.BaseWebTest;

import static model.PartnerStatus.*;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.USERDATA)    @Tag(AllureTag.USERDATA)
@Story("Get partners")
class GetPartnersWebTest extends BaseWebTest {

    @Test
    @DisplayName("Get all users (by status)")
    @ApiLogin(user = @GenerateUser(partners = {
            @WithPartner(status = NOT_FRIEND),
            @WithPartner(status = NOT_FRIEND, user = @WithUser(avatarClasspath = "img/png.png")),
            @WithPartner(status = INVITATION_SENT, user = @WithUser(avatarClasspath = "img/jpeg.jpeg")),
            @WithPartner(status = INVITATION_RECEIVED),
            @WithPartner(status = INVITATION_RECEIVED, user = @WithUser(avatarClasspath = "img/jpg.jpg")),
            @WithPartner(status = FRIEND)}))
    void getAllUsersTest(@Inject UserModel user) {
        mainPage.openPeopleAroundSection();
        for (PartnerStatus status : user.getPartners().keySet()) {
            user.getPartners().get(status).forEach(
                    partner -> peopleAroundPage.verifyPartnerInformation(partner, status));
        }
    }

    @Test
    @DisplayName("Get user friends")
    @ApiLogin(user = @GenerateUser(partners = {
            @WithPartner(status = FRIEND),
            @WithPartner(status = FRIEND, user = @WithUser(avatarClasspath = "img/png.png")),
            @WithPartner(status = INVITATION_SENT),
            @WithPartner(status = INVITATION_RECEIVED)}))
    void getUserFriendsTest(@Inject UserModel user) {
        mainPage.getHeader().verifyFriendsCount(user.getFriends().size())
                .openFriendsModalWindow();
        user.getFriends().forEach(friendsComponent::verifyFriendInformation);
    }

}
