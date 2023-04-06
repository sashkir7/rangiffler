package gateway.service.api;

import gateway.model.PartnerStatus;
import gateway.model.UserDto;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Set;

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

    public void inviteToFriends(@Nonnull String username,
                                @Nonnull UserDto friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9001/users/invite").queryParams(params).build().toUri();

        webClient.post()
                .uri(uri)
                .body(Mono.just(friend), UserDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void submitFriend(@Nonnull String username,
                             @Nonnull UserDto friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9001/friends/submit").queryParams(params).build().toUri();

        webClient.post()
                .uri(uri)
                .body(Mono.just(friend), UserDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void declineFriend(@Nonnull String username,
                              @Nonnull UserDto friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9001/friends/decline").queryParams(params).build().toUri();

        webClient.post()
                .uri(uri)
                .body(Mono.just(friend), UserDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void removeFriend(@Nonnull String username,
                             @Nonnull UserDto friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:9001/friends/remove").queryParams(params).build().toUri();

        webClient.post()
                .uri(uri)
                .body(Mono.just(friend), UserDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
