package test.api.userdata;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import jupiter.annotation.GenerateUser;
import jupiter.annotation.Inject;
import jupiter.annotation.WithPartner;
import jupiter.annotation.WithUser;
import model.PartnerStatus;
import model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import sashkir7.grpc.RelationshipResponse;
import sashkir7.grpc.User;
import sashkir7.grpc.Users;
import test.api.BaseApiTest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.qameta.allure.Allure.step;
import static model.PartnerStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic(AllureEpic.API)               @Tag(AllureTag.API)
@Feature(AllureFeature.USERDATA)    @Tag(AllureTag.USERDATA)
@Story("Partners relationship operations")
class UserdataPartnersApiTest extends BaseApiTest {

    @Test
    @DisplayName("Get all users")
    @GenerateUser(partners = {
            @WithPartner(status = NOT_FRIEND),
            @WithPartner(status = INVITATION_SENT),
            @WithPartner(status = INVITATION_RECEIVED),
            @WithPartner(status = FRIEND)})
    void getAllUsersTest(@Inject UserModel user) {
        Map<String, Users> allUsers = userdataApi.getAllUsers(user.getUsername()).getUsersMap();
        for (PartnerStatus status : PartnerStatus.values()) {
            List<String> actualUsernamesByStatus = convertToListNicknames(allUsers.get(status.toString()));
            List<String> expectedUsernamesByStatus = convertToListNicknames(user.getPartners().get(status));

            if (status == NOT_FRIEND) {
                step("Verify that not friends list contains partner user", () ->
                        assertThat(actualUsernamesByStatus)
                                .contains(expectedUsernamesByStatus.toArray(String[]::new)));
            } else {
                step("Verify partners by status " + status, () ->
                        assertThat(actualUsernamesByStatus)
                                .hasSameElementsAs(expectedUsernamesByStatus));
            }
        }
    }

    @Test
    @DisplayName("Get friends")
    @GenerateUser(partners = {
            @WithPartner(status = NOT_FRIEND),
            @WithPartner(status = INVITATION_SENT),
            @WithPartner(status = INVITATION_RECEIVED),
            @WithPartner(status = FRIEND),
            @WithPartner(status = FRIEND)})
    void getFriendsTest(@Inject UserModel user) {
        Map<String, Users> allUsers = userdataApi.getAllUsers(user.getUsername()).getUsersMap();
        List<String> actualFriends = convertToListNicknames(allUsers.get(FRIEND.toString()));
        List<String> expectedFriends = convertToListNicknames(user.getPartners().get(FRIEND));

        step("Verify user friends", () ->
                assertThat(actualFriends).hasSameElementsAs(expectedFriends));
    }

    @Test
    @DisplayName("Invite to friends")
    void inviteToFriendsTest(@WithUser User user, @WithUser User partner) {
        inviteToFriends(user, partner);

        step("Verify invite to friends", () -> {
            step("Verify from user side", () ->
                    assertThat(getUserPartnersByStatus(user, INVITATION_SENT))
                            .isEqualTo(List.of(partner)));
            step("Verify from partner side", () ->
                    assertThat(getUserPartnersByStatus(partner, INVITATION_RECEIVED))
                            .isEqualTo(List.of(user)));
        });
    }

    @Test
    @DisplayName("Submit friends")
    void submitFriendsTest(@WithUser User user, @WithUser User partner) {
        inviteToFriends(partner, user);
        submitFriends(user, partner);

        step("Verify submit friends", () -> {
            step("Verify from user side", () ->
                    assertThat(getUserPartnersByStatus(user, FRIEND))
                            .isEqualTo(List.of(partner)));
            step("Verify from partner side", () -> {
                assertThat(getUserPartnersByStatus(partner, FRIEND))
                        .isEqualTo(List.of(user));
            });
        });
    }

    @Test
    @DisplayName("Decline friend")
    void declineFriendTest(@WithUser User user, @WithUser User partner) {
        inviteToFriends(partner, user);
        step("Decline friend", () ->
                userdataApi.declineFriend(user.getUsername(), partner));

        step("Verify decline friend", () -> {
            step("Verify from user side", () ->
                    assertThat(getUserPartnersByStatus(user, NOT_FRIEND))
                            .contains(partner));
            step("Verify from partner side", () ->
                    assertThat(getUserPartnersByStatus(partner, NOT_FRIEND))
                            .contains(user));
        });
    }

    @Test
    @DisplayName("Remove friend")
    void removeFriendTest(@WithUser User user, @WithUser User partner) {
        step("Add to friends", () -> {
            inviteToFriends(user, partner);
            submitFriends(partner, user);
        });

        step("Remove friend", () ->
                userdataApi.removeFriend(user.getUsername(), partner));

        step("Verify remove friend", () -> {
            step("Verify from user side", () ->
                    assertThat(getUserPartnersByStatus(user, NOT_FRIEND))
                            .contains(partner));
            step("Verify from partner side", () ->
                    assertThat(getUserPartnersByStatus(partner, NOT_FRIEND))
                            .contains(user));
        });
    }

    private List<User> getUserPartnersByStatus(User user, PartnerStatus status) {
        return userdataApi.getAllUsers(user.getUsername()).getUsersMap().get(status.toString()).getUsersList();
    }

    @Step("Invite to friends")
    private void inviteToFriends(User user, User partner) {
        RelationshipResponse response = userdataApi.inviteToFriends(user.getUsername(), partner);
        step("Verify response", () -> {
            assertEquals(user, response.getUser());
            assertEquals(partner, response.getPartner());
            assertEquals(INVITATION_SENT.toString(), response.getStatus());
        });
    }

    @Step("Submit friends")
    private void submitFriends(User user, User partner) {
        RelationshipResponse response = userdataApi.submitFriends(user.getUsername(), partner);
        step("Verify response", () -> {
            assertEquals(user, response.getUser());
            assertEquals(partner, response.getPartner());
            assertEquals(FRIEND.toString(), response.getStatus());
        });
    }

    private List<String> convertToListNicknames(Users users) {
        return users.getUsersList().stream().map(User::getUsername).toList();
    }

    private List<String> convertToListNicknames(Collection<UserModel> users) {
        return users.stream().map(UserModel::getUsername).toList();
    }

}
