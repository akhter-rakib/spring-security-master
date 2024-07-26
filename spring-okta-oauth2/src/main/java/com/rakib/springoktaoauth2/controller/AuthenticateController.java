package com.rakib.springoktaoauth2.controller;

import com.rakib.springoktaoauth2.model.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthenticateController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello, this is a public endpoint!";
    }

    // Secure endpoint
    @GetMapping("/secure/hello")
    public ResponseEntity<AuthenticationResponse> secureHello(
            @AuthenticationPrincipal OidcUser oidcUser,
            Model model,
            @RegisteredOAuth2AuthorizedClient("okta")
            OAuth2AuthorizedClient client

    ) {
        AuthenticationResponse authenticationResponse
                = AuthenticationResponse.builder()
                .userId(oidcUser.getEmail())
                .accessToken(client.getAccessToken().getTokenValue())
                .refreshToken(Objects.requireNonNull(client.getRefreshToken()).getTokenValue())
                .expiresAt(Objects.requireNonNull(client.getAccessToken().getExpiresAt()).getEpochSecond())
                .authorityList(oidcUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();// Using Collectors.toList() for compatibility
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("secure/admin/hello")
    public String adminHello() {
        return "Hello, Admin! This endpoint is secured.";
    }

}
