package api.auth.context;

import helper.LoginHelper;

import java.util.HashMap;
import java.util.Map;

public class SessionStorageHolder {

    private static final String CODE_VERIFIER_KEY = "codeVerifier",
            CODE_CHALLENGE_KEY = "codeChallenge",
            CODE_KEY = "code",
            ID_TOKEN_KEY = "id_token";
    private static final ThreadLocal<SessionStorageHolder> INSTANCE = ThreadLocal.withInitial(SessionStorageHolder::new);

    public static SessionStorageHolder getInstance() {
        return INSTANCE.get();
    }

    private final Map<String, String> sessionStorage;

    private SessionStorageHolder() {
        sessionStorage = new HashMap<>();
    }

    public void init() {
        final String codeVerifier = LoginHelper.generateCodeVerifier();
        sessionStorage.put(CODE_VERIFIER_KEY, codeVerifier);
        sessionStorage.put(CODE_CHALLENGE_KEY, LoginHelper.generateCodeChallenge(codeVerifier));
    }

    public void addCode(String code) {
        sessionStorage.put(CODE_KEY, code);
    }

    public String getCode() {
        return sessionStorage.get(CODE_KEY);
    }

    public void addToken(String token) {
        sessionStorage.put(ID_TOKEN_KEY, token);
    }

    public String getToken() {
        return sessionStorage.get(ID_TOKEN_KEY);
    }

    public String getCodeVerifier() {
        return sessionStorage.get(CODE_VERIFIER_KEY);
    }

    public String getCodeChallenge() {
        return sessionStorage.get(CODE_CHALLENGE_KEY);
    }

    public void flushAll() {
        sessionStorage.clear();
    }

}
