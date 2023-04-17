package test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import pages.LoginPage;
import pages.RegistrationPage;
import test.BaseTest;

public abstract class BaseWebTest extends BaseTest {

    protected final LoginPage loginPage = new LoginPage();
    protected final RegistrationPage registrationPage = new RegistrationPage();

    @AfterEach
    // ToDo - убрать в extension
    void a1() {
        Selenide.closeWebDriver();
    }

}
