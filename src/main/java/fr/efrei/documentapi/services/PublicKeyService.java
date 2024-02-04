package fr.efrei.documentapi.services;

import fr.efrei.documentapi.models.AuthKey;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public interface PublicKeyService {
    public PublicKey retrievePublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException;
}
