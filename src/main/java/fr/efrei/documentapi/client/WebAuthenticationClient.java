package fr.efrei.documentapi.client;

import fr.efrei.documentapi.models.AuthKey;
import reactor.core.publisher.Mono;

public interface WebAuthenticationClient {
    public Mono<AuthKey> getPublicKey();
}
