package fr.efrei.documentapi.services;

import fr.efrei.documentapi.security.user.UserDetailsImpl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface TokenService {
    public UserDetailsImpl findUserDetailsFromToken(String authToken) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
