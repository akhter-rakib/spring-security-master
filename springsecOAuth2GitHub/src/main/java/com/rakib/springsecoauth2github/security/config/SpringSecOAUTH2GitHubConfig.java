package com.rakib.springsecoauth2github.security.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.web.*;

@Configuration
public class SpringSecOAUTH2GitHubConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request ->
                        request.anyRequest()
                                .authenticated())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }


    /*This below code not needed because it can be handled by application properties*/
   /* @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientReg = clientRegistration();
        return new InMemoryClientRegistrationRepository(clientReg);
    }

    private ClientRegistration clientRegistration() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("97f9e9955b3fe124eb80")
                .clientSecret("79c3662c7afbb26a428c167f8c1df0993a31801a").build();
    }*/
}
