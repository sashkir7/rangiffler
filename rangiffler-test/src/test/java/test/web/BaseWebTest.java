package test.web;

import jupiter.extension.SelenideConfigurationExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.*;
import pages.components.PhotoComponent;
import pages.components.FriendsComponent;
import pages.components.HeaderComponent;
import test.BaseTest;

@ExtendWith(SelenideConfigurationExtension.class)
public abstract class BaseWebTest extends BaseTest {

    protected final LandingPage landingPage = new LandingPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final RegistrationPage registrationPage = new RegistrationPage();
    protected final TravelsPage travelsPage = new TravelsPage();
    protected final PeopleAroundPage peopleAroundPage = new PeopleAroundPage();
    protected final HeaderComponent headerComponent = new HeaderComponent();
    protected final FriendsComponent friendsComponent = new FriendsComponent();
    protected final PhotoComponent photoComponent = new PhotoComponent();

}
