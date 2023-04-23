package jupiter.extension;

import api.auth.context.CookieHolder;
import api.auth.context.SessionStorageHolder;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import config.AppProperties;
import jupiter.annotation.ApiLogin;
import model.UserModel;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import static io.qameta.allure.Allure.step;

public class ApiLoginExtension extends BaseJUnitExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin annotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);

        String username, password;
        if (annotation.user().handleAnnotation()) {
            // Login through user model created via @GenerateUser annotation
            UserModel user = getFromStore(context, GenerateUserExtension.NAMESPACE, UserModel.class);
            username = user.getUsername();
            password = user.getPassword();
        } else {
            // Login via username + password
            if (annotation.username().isBlank())
                throw new IllegalArgumentException("Username cannot be empty");
            username = annotation.username();

            if (annotation.password().isBlank())
                throw new IllegalArgumentException("Password cannot be empty");
            password = annotation.password();
        }

        step("Api login with creds: username = " + username + ", password = " + password, () -> {
            apiLogin(username, password);
            Selenide.open(AppProperties.APP_BASE_URL);
            SessionStorage sessionStorage = Selenide.sessionStorage();
            sessionStorage.setItem("codeChallenge", SessionStorageHolder.getInstance().getCodeChallenge());
            sessionStorage.setItem("id_token", SessionStorageHolder.getInstance().getToken());
            sessionStorage.setItem("codeVerifier", SessionStorageHolder.getInstance().getCodeVerifier());

            WebDriverRunner.getWebDriver().manage()
                    .addCookie(new Cookie("JSESSIONID", CookieHolder.getInstance().getCookieValueByPart("JSESSIONID")));
            Selenide.refresh();
        });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CookieHolder.getInstance().flushAll();
        SessionStorageHolder.getInstance().flushAll();
    }

    private void apiLogin(String username, String password) throws Exception {
        authApi.login(username, password);
        JsonNode token = authApi.getToken();
        SessionStorageHolder.getInstance().addToken(token.get("id_token").asText());
    }

}
