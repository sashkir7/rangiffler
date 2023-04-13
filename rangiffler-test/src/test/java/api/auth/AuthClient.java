package api.auth;

import api.auth.context.CookieHolder;
import api.auth.context.SessionStorageHolder;
import api.auth.interceptops.AddCookiesReqInterceptor;
import api.auth.interceptops.ExtractCodeFromRespInterceptor;
import api.auth.interceptops.ReceivedCookieRespInterceptor;
import model.UserModel;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
            .baseUrl("http://127.0.0.1:9000/")
            .build();

    private final AuthService authService = retrofit.create(AuthService.class);

    private void authorize() throws IOException {
        SessionStorageHolder.getInstance().init();
        authService.authorize(
                "code",
                "client",
                "openid",
                "http://127.0.0.1:9000/" + "authorized",
                SessionStorageHolder.getInstance().getCodeChallenge(),
                "S256"
        ).execute();
    }

    public void register(String username, String password, String firstname, String lastname) throws IOException {
        authorize();
        Response<Void> response = authService.register(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username, password, password, firstname, lastname
        ).execute();
        assertEquals(201, response.code());
        assertNull(response.errorBody());
    }

    public void register(UserModel userModel) throws IOException {
        register(userModel.getUsername(), userModel.getPassword(), userModel.getFirstname(), userModel.getLastname());
    }

//    public Response<Void> login(String username, String password) throws Exception {
//        return authService.login(
//                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
//                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
//                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
//                username,
//                password
//        ).execute();
//    }
//
//
//    public JsonNode getToken() throws Exception {
//        String basic = "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
//        return authService.getToken(
//                basic,
//                "client",
//                "http://127.0.0.1:3001/" + "authorized",
//                "authorization_code",
//                SessionStorageHolder.getInstance().getCode(),
//                SessionStorageHolder.getInstance().getCodeVerifier()
//        ).execute().body();
//    }

}
