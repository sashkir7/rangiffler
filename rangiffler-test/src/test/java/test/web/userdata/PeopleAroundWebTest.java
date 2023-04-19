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
import org.junit.jupiter.api.*;
import sashkir7.grpc.User;
import sashkir7.grpc.Users;
import test.web.BaseWebTest;

import java.util.Map;

import static model.PartnerStatus.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.USERDATA)    @Tag(AllureTag.USERDATA)
@Story("People around")
class PeopleAroundWebTest extends BaseWebTest {

    @BeforeEach
    void openPeopleAroundSection() {
        mainPage.openPeopleAroundSection();
    }

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
        for (PartnerStatus status : user.getPartners().keySet()) {
            user.getPartners().get(status).forEach(
                    partner -> peopleAroundPage.verifyPartnerInformation(partner, status));
        }
    }

    @Test
    @DisplayName("Invite to friend")
    @ApiLogin(user = @GenerateUser(partners = @WithPartner(status = NOT_FRIEND)))
    void inviteToFriendTest(@Inject UserModel user) {
        UserModel partner = findPartnerFromUserPartners(user, NOT_FRIEND);
        peopleAroundPage.inviteToFriends(partner.getUsername());

        assertNotNull(getAllUsersAndFindPartner(user, partner, INVITATION_SENT));
        assertNotNull(getAllUsersAndFindPartner(partner, user, INVITATION_RECEIVED));
    }

    @Test
    @DisplayName("Submit friend")
    @ApiLogin(user = @GenerateUser(partners = @WithPartner(status = INVITATION_RECEIVED)))
    void submitFriendTest(@Inject UserModel user) {
        UserModel partner = findPartnerFromUserPartners(user, INVITATION_RECEIVED);
        peopleAroundPage.submitFriend(partner.getUsername());

        assertNotNull(getAllUsersAndFindPartner(user, partner, FRIEND));
        assertNotNull(getAllUsersAndFindPartner(partner, user, FRIEND));
    }

    @Test
    @DisplayName("Decline friend")
    @ApiLogin(user = @GenerateUser(partners = @WithPartner(status = INVITATION_RECEIVED)))
    void declineFriendTest(@Inject UserModel user) {
        UserModel partner = findPartnerFromUserPartners(user, INVITATION_RECEIVED);
        peopleAroundPage.declineFriend(partner.getUsername());

        assertNotNull(getAllUsersAndFindPartner(user, partner, NOT_FRIEND));
        assertNotNull(getAllUsersAndFindPartner(partner, user, NOT_FRIEND));
    }

    @Test
    @DisplayName("Remove friend")
    @ApiLogin(user = @GenerateUser(partners = @WithPartner(status = FRIEND)))
    void removeFriendTest(@Inject UserModel user) {
        UserModel partner = findPartnerFromUserPartners(user, FRIEND);
        peopleAroundPage.removeFriend(partner.getUsername());

        assertNotNull(getAllUsersAndFindPartner(user, partner, NOT_FRIEND));
        assertNotNull(getAllUsersAndFindPartner(partner, user, NOT_FRIEND));
    }

    private UserModel findPartnerFromUserPartners(UserModel user, PartnerStatus status) {
        return user.getPartners().get(status).stream().findFirst().orElseThrow();
    }

    private User getAllUsersAndFindPartner(UserModel user, UserModel partner, PartnerStatus status) {
        Map<String, Users> allUsers = userdataApi.getAllUsers(user.getUsername()).getUsersMap();
        return allUsers.get(status.toString()).getUsersList().stream()
                .filter(p -> p.getUsername().equals(partner.getUsername()))
                .findFirst().orElse(null);
    }

}
