package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/app.properties")
interface AppConfig extends Config {

    @Key("auth.base.url")
    String authBaseUrl();
    @Key("app.base.url")
    String appBaseUrl();

    @Key("userdata.service.host")
    String userdataServiceHost();
    @Key("userdata.service.port")
    Integer userdataServicePort();

    @Key("geo.service.host")
    String geoServiceHost();
    @Key("geo.service.port")
    Integer geoServicePort();

    @Key("photo.service.host")
    String photoServiceHost();
    @Key("photo.service.port")
    Integer photoServicePort();

}
