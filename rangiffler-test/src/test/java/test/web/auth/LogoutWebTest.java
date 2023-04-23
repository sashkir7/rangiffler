package test.web.auth;

import allure.AllureEpic;
import allure.AllureFeature;
import allure.AllureTag;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import jupiter.annotation.ApiLogin;
import jupiter.annotation.GenerateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import test.web.BaseWebTest;

import static io.qameta.allure.Allure.step;

@Epic(AllureEpic.WEB)           @Tag(AllureTag.WEB)
@Feature(AllureFeature.AUTH)    @Tag(AllureTag.AUTH)
@Story("Logout")
class LogoutWebTest extends BaseWebTest {

    @Test
    @ApiLogin(user = @GenerateUser)
    @DisplayName("Logout")
    void logoutTest() {
        step("Verify that user is success login",
                headerComponent::verifyTitleIsRangiffler);
        step("Logout", headerComponent::clickLogoutIcon);
        step("Verify that user success logout", () -> {
            landingPage.verifyPageIsLoaded()
                    .clickLoginButton();
            loginPage.verifyPageIsLoaded();
        });
    }

}
