package config;

import org.aeonbits.owner.ConfigFactory;

public final class DatabaseProperties {

    private static final DatabaseConfig CONFIG = ConfigFactory.create(DatabaseConfig.class);

    public static final String DATABASE_USERNAME = CONFIG.databaseUsername(),
            DATABASE_PASSWORD = CONFIG.databasePassword();

    public static final String AUTH_DATABASE_URL = CONFIG.authDatabaseUrl(),
            USERDATA_DATABASE_URL = CONFIG.userdataDatabaseUrl(),
            GEO_DATABASE_URL = CONFIG.geoDatabaseUrl(),
            PHOTO_DATABASE_URL = CONFIG.photoDatabaseUrl();

}
