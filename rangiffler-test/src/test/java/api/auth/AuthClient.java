package api.auth;

import api.auth.context.CookieHolder;
import api.auth.context.SessionStorageHolder;
import api.auth.interceptops.AddCookiesReqInterceptor;
import api.auth.interceptops.ExtractCodeFromRespInterceptor;
import api.auth.interceptops.ReceivedCookieRespInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import config.AppProperties;
import model.UserModel;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthClient {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .followRedirects(true)
            .addNetworkInterceptor(new ReceivedCookieRespInterceptor())
            .addNetworkInterceptor(new AddCookiesReqInterceptor())
            .addNetworkInterceptor(new ExtractCodeFromRespInterceptor())
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(HTTP_CLIENT)
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(AppProperties.AUTH_BASE_URL)
            .build();

    private final AuthService authService = retrofit.create(AuthService.class);

    private void authorize() throws Exception {
        SessionStorageHolder.getInstance().init();
        authService.authorize(
                "code",
                "client",
                "openid",
                AppProperties.APP_BASE_URL + "/authorized",
                SessionStorageHolder.getInstance().getCodeChallenge(),
                "S256"
        ).execute();
    }

    public Response<Void> login(String username, String password) throws Exception {
        authorize();
        return authService.login(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username, password
        ).execute();
    }

    public JsonNode getToken() throws Exception {
        String basic = "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
        return authService.getToken(
                basic,
                "client",
                AppProperties.APP_BASE_URL + "/authorized",
                "authorization_code",
                SessionStorageHolder.getInstance().getCode(),
                SessionStorageHolder.getInstance().getCodeVerifier()
        ).execute().body();
    }

    public Response<Void> register(String username, String password, String firstname, String lastname) throws Exception {
        authorize();
        return authService.register(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username, password, password,
                firstname, lastname
        ).execute();
    }

    public void register(UserModel userModel) throws Exception {
        register(userModel.getUsername(), userModel.getPassword(), userModel.getFirstname(), userModel.getLastname());
    }

}
