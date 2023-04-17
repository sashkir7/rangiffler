package config;

import org.aeonbits.owner.ConfigFactory;

public final class AppProperties {

    private static final AppConfig CONFIG = ConfigFactory.create(AppConfig.class);

    public static final String AUTH_BASE_URL = CONFIG.authBaseUrl(),
            APP_BASE_URL = CONFIG.appBaseUrl();

}
