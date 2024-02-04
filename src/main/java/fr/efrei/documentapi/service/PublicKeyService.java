package fr.efrei.documentapi.service;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public interface PublicKeyService {
    public PublicKey retrievePublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException;
}
