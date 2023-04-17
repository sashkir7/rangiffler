package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/database.properties")
interface DatabaseConfig extends Config {

    @Key("database.username")
    String databaseUsername();

    @Key("database.password")
    String databasePassword();

    @Key("auth.database.url")
    String authDatabaseUrl();

    @Key("userdata.database.url")
    String userdataDatabaseUrl();

    @Key("geo.database.url")
    String geoDatabaseUrl();

    @Key("photo.database.url")
    String photoDatabaseUrl();

}
