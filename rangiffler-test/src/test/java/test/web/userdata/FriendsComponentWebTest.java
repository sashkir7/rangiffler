package test.web.userdata;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.*;
import model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sashkir7.grpc.Users;
import test.web.BaseWebTest;

import static io.qameta.allure.Allure.step;
import static model.PartnerStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic(AllureEpic.WEB)               @Tag(AllureTag.WEB)
@Feature(AllureFeature.USERDATA)    @Tag(AllureTag.USERDATA)
@Story("Friends component")
class FriendsComponentWebTest extends BaseWebTest {

    @BeforeEach
    void openFriendsModalWindow() {
        headerComponent.openFriendsModalWindow();
    }

    @Test
    @DisplayName("Get user friends")
    @ApiLogin(user = @GenerateUser(partners = {
            @WithPartner(status = FRIEND),
            @WithPartner(status = FRIEND, user = @WithUser(avatarClasspath = "img/dog.png")),
            @WithPartner(status = INVITATION_SENT),
            @WithPartner(status = INVITATION_RECEIVED)}))
    void getUserFriendsTest(@Inject UserModel user) {
        step("Verify friends information in header", () ->
                headerComponent.verifyFriendsCount(user.getFriends().size()));
        step("Verify friends information in friends modal window", () ->
                user.getFriends().forEach(friendsComponent::verifyFriendInformation));
    }

    @Test
    @DisplayName("Remove friend")
    @ApiLogin(user = @GenerateUser(partners = {@WithPartner, @WithPartner}))
    void removeFriendTest(@Inject UserModel user) {
        int friendsCount = user.getFriends().size();
        step("Remove friend", () ->
                friendsComponent.removeFriend(findFirstFriend(user).getUsername()));
        step("Verify that friend has been deleted (UI)", () ->
                headerComponent.verifyFriendsCount(friendsCount - 1));
        step("Verify that friend has been deleted (API)", () -> {
            Users actualFriends = userdataApi.getFriends(user.getUsername());
            assertEquals(friendsCount - 1, actualFriends.getUsersList().size());
        });
    }

    private UserModel findFirstFriend(UserModel user) {
        return user.getFriends().stream().findFirst().orElseThrow();
    }

}
