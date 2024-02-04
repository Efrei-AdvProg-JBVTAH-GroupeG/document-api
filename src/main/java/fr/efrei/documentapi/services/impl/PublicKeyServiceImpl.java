package fr.efrei.documentapi.services.impl;

import fr.efrei.documentapi.client.WebAuthenticationClient;
import fr.efrei.documentapi.models.AuthKey;
import fr.efrei.documentapi.services.PublicKeyService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class PublicKeyServiceImpl implements PublicKeyService {

    private WebAuthenticationClient authenticationClient;

    public PublicKeyServiceImpl(
            WebAuthenticationClient authenticationClient
    ) {
        this.authenticationClient = authenticationClient;
    }

    @Override @Cacheable(value = "publicKey")
    public PublicKey retrievePublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        AuthKey authKey = authenticationClient.getPublicKey().block();

        assert authKey != null;
        byte[] publicBytes = Base64.getDecoder().decode(authKey.getToken());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
