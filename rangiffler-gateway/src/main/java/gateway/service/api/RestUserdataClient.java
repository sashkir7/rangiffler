package gateway.service.api;

import gateway.model.UserJson;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class RestUserdataClient {

    private final WebClient webClient;
    private final String userdataBaseUri;

    @Autowired
    public RestUserdataClient(WebClient webClient
//                              @Value("${niffler-userdata.base-uri}") String nifflerUserdataBaseUri
    ) {
        this.webClient = webClient;
        // ToDo Из конфига
        this.userdataBaseUri = "http://127.0.0.1:9001";
    }

    public @Nonnull UserJson currentUser(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        // ToDo Из конфига
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9001/currentUser")
                .queryParams(params)
                .build()
                .toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public @Nonnull UserJson updateUserInfo(@Nonnull UserJson user) {
        return webClient.post()
                .uri("http://localhost:9001/currentUser")
                .body(Mono.just(user), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

}
