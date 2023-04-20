package test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import pages.*;
import pages.components.UploadPhotoComponent;
import pages.components.FriendsComponent;
import pages.components.HeaderComponent;
import test.BaseTest;

public abstract class BaseWebTest extends BaseTest {

    protected final LandingPage landingPage = new LandingPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final RegistrationPage registrationPage = new RegistrationPage();
    protected final YourTravelsPage yourTravelsPage = new YourTravelsPage();
    protected final PeopleAroundPage peopleAroundPage = new PeopleAroundPage();
    protected final HeaderComponent headerComponent = new HeaderComponent();
    protected final FriendsComponent friendsComponent = new FriendsComponent();
    protected final UploadPhotoComponent uploadPhotoComponent = new UploadPhotoComponent();

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
