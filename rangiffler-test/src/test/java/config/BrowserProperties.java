package config;

import org.aeonbits.owner.ConfigFactory;

public final class BrowserProperties {

    private static final BrowserConfig CONFIG = ConfigFactory.create(BrowserConfig.class);

    public static final String BROWSER_NAME = CONFIG.browserName(),
            BROWSER_VERSION = CONFIG.browserVersion(),
            BROWSER_SIZE = CONFIG.browserSize();

}
