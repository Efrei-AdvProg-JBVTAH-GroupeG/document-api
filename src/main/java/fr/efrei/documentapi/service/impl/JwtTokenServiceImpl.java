package fr.efrei.documentapi.service.impl;

import fr.efrei.documentapi.security.user.UserDetailsImpl;
import fr.efrei.documentapi.service.PublicKeyService;
import fr.efrei.documentapi.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JwtTokenServiceImpl implements TokenService {

    private PublicKeyService publicKeyService;

    public JwtTokenServiceImpl(
            PublicKeyService publicKeyService
    ) {
        this.publicKeyService = publicKeyService;
    }

    @Override
    public UserDetailsImpl findUserDetailsFromToken(String authToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Jws<Claims> jws = Jwts.parser().verifyWith(publicKeyService.retrievePublicKey()).build()
                .parseSignedClaims(authToken);

        Object roles = jws.getPayload().get("roles");
        List<GrantedAuthority> roleList = new ArrayList<>();
        if(roles instanceof ArrayList<?> list) {
            for (Object el:list) {
                if(el instanceof String elStr) {
                    roleList.add(new SimpleGrantedAuthority(elStr));
                }
            }
        }

        return new UserDetailsImpl(
                jws.getPayload().get("id", Long.class),
                jws.getPayload().getSubject(),
                jws.getPayload().get("email", String.class),
                roleList
        );
    }
}
