package config;

import org.aeonbits.owner.ConfigFactory;

public final class AppProperties {

    private static final AppConfig CONFIG = ConfigFactory.create(AppConfig.class);

    public static final String AUTH_BASE_URL = CONFIG.authBaseUrl(),
            APP_BASE_URL = CONFIG.appBaseUrl();

    public static final String USERDATA_SERVICE_HOST = CONFIG.userdataServiceHost(),
            GEO_SERVICE_HOST = CONFIG.geoServiceHost(),
            PHOTO_SERVICE_HOST = CONFIG.photoServiceHost();

    public static final Integer USERDATA_SERVICE_PORT = CONFIG.userdataServicePort(),
            GEO_SERVICE_PORT = CONFIG.geoServicePort(),
            PHOTO_SERVICE_PORT = CONFIG.photoServicePort();

}
