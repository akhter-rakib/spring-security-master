package com.rakib.springsecoauth2github.controller;

import org.springframework.security.oauth2.client.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class SecurityController {


    @GetMapping
    public String securePage(OAuth2AuthenticationToken token) {
        System.out.println(token.getPrincipal());
        return "secure.html";
    }
}
