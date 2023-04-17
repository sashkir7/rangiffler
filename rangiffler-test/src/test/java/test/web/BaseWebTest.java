package test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import pages.LoginPage;
import pages.LandingPage;
import pages.MainPage;
import pages.RegistrationPage;
import test.BaseTest;

public abstract class BaseWebTest extends BaseTest {

    protected final LandingPage landingPage = new LandingPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final RegistrationPage registrationPage = new RegistrationPage();
    protected final MainPage mainPage = new MainPage();


    @BeforeAll
    static void ab() {
        Configuration.browserSize = "1920x1080";
    }

    @AfterEach
    // ToDo - убрать в extension
    void a1() {
        Selenide.closeWebDriver();
    }

}
