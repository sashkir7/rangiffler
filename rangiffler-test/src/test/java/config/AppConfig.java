package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/app.properties")
interface AppConfig extends Config {

    @Key("auth.base.url")
    String authBaseUrl();

    @Key("app.base.url")
    String appBaseUrl();

}
