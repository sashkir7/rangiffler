package test.api.userdata;

import allure.AllureEpic;
import allure.AllureStory;
import allure.AllureTag;
import data.HibernateUserdataRepository;
import data.repository.UserdataRepository;
import helper.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sashkir7.grpc.User;
import test.BaseTest;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@Epic(AllureEpic.API)           @Tag(AllureTag.API)
@Story(AllureStory.USERDATA)    @Tag(AllureTag.USERDATA)
@Feature("CRUD operations")
class UserdataCrudApiTest extends BaseTest {

    private static final String AVATAR_CLASSPATH = "img/dog_01.jpeg";

    @Test
    @DisplayName("Get current user")
    void getCurrentUserTest() {
        User user = userdataApi.addUser(getRandomUser(true));
        verifyUser(user, userdataApi.getUserByUsername(user.getUsername()));
    }

    @ValueSource(booleans = {true, false})
    @ParameterizedTest(name = "avatar is {0}")
    @DisplayName("Add new user:")
    void createNewUserTest(boolean withAvatar) {
        User user = getRandomUser(withAvatar);
        verifyUser(user, userdataApi.addUser(user));
    }

    @Test
    @DisplayName("Update user")
    void updateUserTest() {
        User user = userdataApi.addUser(getRandomUser(true));
        User modifierUser = getRandomModifierUser(user);
        verifyUser(modifierUser, userdataApi.updateUser(modifierUser));
    }

    @Test
    @DisplayName("Delete user")
    void deleteUserTest() {
        UserdataRepository repository = new HibernateUserdataRepository();
        User user = userdataApi.addUser(getRandomUser(false));
        userdataApi.deleteUser(user.getUsername());

        step("Verify that user has been deleted", () ->
                assertNull(repository.findByUsername(user.getUsername())));
    }


    private User getRandomUser(boolean withAvatar) {
        User.Builder builder = User.newBuilder()
                .setUsername(DataHelper.randomUsername())
                .setFirstname(DataHelper.randomFirstname())
                .setLastname(DataHelper.randomLastname());
        if (withAvatar) {
            builder.setAvatar(DataHelper.imageByClasspath(AVATAR_CLASSPATH));
        }
        return builder.build();
    }

    private User getRandomModifierUser(User existsUser) {
        return User.newBuilder()
                .setId(existsUser.getId())
                .setUsername(existsUser.getUsername())
                .setFirstname(DataHelper.randomFirstname())
                .setLastname(DataHelper.randomLastname())
                .build();
    }

    @Step("Verify created user")
    private void verifyUser(User expected, User actual) {
        if ("".equals(expected.getId())) {
            // Create new user
            step("Verify id is not null", () ->
                    assertNotEquals("", actual.getId()));
        } else {
            // Update exists user
            step("Verify id", () ->
                    assertEquals(expected.getId(), actual.getId()));
        }

        step("Verify username", () ->
                assertEquals(expected.getUsername(), actual.getUsername()));
        step("Verify firstname", () ->
                assertEquals(expected.getFirstname(), actual.getFirstname()));
        step("Verify lastname", () ->
                assertEquals(expected.getLastname(), actual.getLastname()));
        step("Verify avatar", () ->
                assertEquals(expected.getAvatar(), actual.getAvatar()));
    }

}
