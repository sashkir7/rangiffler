package test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import test.BaseTest;

public abstract class BaseWebTest extends BaseTest {

    @AfterEach
    // ToDo - убрать в extension
    void a1() {
        Selenide.closeWebDriver();
    }

}
