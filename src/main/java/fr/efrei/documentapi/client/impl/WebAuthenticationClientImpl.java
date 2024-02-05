package fr.efrei.documentapi.client.impl;

import fr.efrei.documentapi.client.WebAuthenticationClient;
import fr.efrei.documentapi.configuration.properties.WebAuthenticationProperties;
import fr.efrei.documentapi.model.AuthKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebAuthenticationClientImpl implements WebAuthenticationClient {

    private final WebClient authenticationWebClient;

    private final WebAuthenticationProperties webAuthenticationProperties;

    public WebAuthenticationClientImpl(
            @Qualifier("authenticationClient") WebClient webClient,
            WebAuthenticationProperties authenticationProperties
    ) {
        this.authenticationWebClient = webClient;
        this.webAuthenticationProperties = authenticationProperties;
    }

    @Override
    public Mono<AuthKey> getPublicKey() {
        return authenticationWebClient.get()
                .uri(webAuthenticationProperties.getPublicKeyUri())
                .retrieve()
                .bodyToMono(AuthKey.class);
    }
}
