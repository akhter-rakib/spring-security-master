package com.rakib.auth0.utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;

@Component
public class AuthUtils {

    private JwkProvider jwkProvider;

    @Value("${auth0.issuer}")
    private String issuer;

    @PostConstruct
    public void initializeJwkProvider() {
        this.jwkProvider = new JwkProviderBuilder(issuer).build();
    }

    public void verifyToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Jwk jwk = jwkProvider.get(jwt.getKeyId());
            RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .ignoreIssuedAt()
                    .build();
            verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
