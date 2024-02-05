package fr.efrei.documentapi.client;

import fr.efrei.documentapi.model.AuthKey;
import reactor.core.publisher.Mono;

public interface WebAuthenticationClient {
    Mono<AuthKey> getPublicKey();
}
