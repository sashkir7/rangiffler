package api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.*;

interface AuthService {

    @GET("/oauth2/authorize")
    Call<Void> authorize(@Query("response_type") String responseType,
                         @Query("client_id") String clientId,
                         @Query("scope") String scope,
                         @Query(value = "redirect_uri", encoded = true) String redirectUri,
                         @Query("code_challenge") String codeChallenge,
                         @Query("code_challenge_method") String codeChallengeMethod);

    @POST("/oauth2/token")
    Call<JsonNode> getToken(@Header("Authorization") String basic,
                            @Query("client_id") String clientId,
                            @Query(value = "redirect_uri", encoded = true) String redirectUri,
                            @Query("grant_type") String grantType,
                            @Query("code") String code,
                            @Query("code_verifier") String codeChallenge);

    @POST("/login")
    @FormUrlEncoded
    Call<Void> login(@Header("Cookie") String jsessionidCookie,
                     @Header("Cookie") String csrfCookie,
                     @Field("_csrf") String csrf,
                     @Field("username") String username,
                     @Field("password") String password);

    @POST("/register")
    @FormUrlEncoded
    Call<Void> register(@Header("Cookie") String jsessionidCookie,
                        @Header("Cookie") String csrfCookie,
                        @Field("_csrf") String csrf,
                        @Field("username") String username,
                        @Field("password") String password,
                        @Field("passwordSubmit") String passwordSubmit,
                        @Field("firstname") String firstname,
                        @Field("lastname") String lastname);

}
