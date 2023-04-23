package jupiter.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import config.BrowserProperties;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SelenideConfigurationExtension implements AroundAllTestsExtension, AfterEachCallback {

    @Override
    public void beforeAllTests(ExtensionContext context) {
        Configuration.browser = BrowserProperties.BROWSER_NAME;
        Configuration.browserVersion = BrowserProperties.BROWSER_VERSION;
        Configuration.browserSize = BrowserProperties.BROWSER_SIZE;
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Selenide.closeWebDriver();
    }

}
