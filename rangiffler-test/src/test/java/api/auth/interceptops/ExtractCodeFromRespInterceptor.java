package api.auth.interceptops;

import api.auth.context.SessionStorageHolder;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class ExtractCodeFromRespInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String code = chain.request().url().queryParameter("code");
        if (code != null) {
            SessionStorageHolder.getInstance().addCode(code);
        }
        return chain.proceed(chain.request());
    }
}
