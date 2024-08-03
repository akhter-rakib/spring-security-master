package com.rakib.auth0.config;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.json.auth.TokenHolder;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.PrototypeAspectInstanceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.time.Instant;


@Service
public class Auth0TokenConfig {
    private static final Logger logger = LoggerFactory.getLogger(Auth0TokenConfig.class);

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    private String managementApiToken;

    private AuthAPI authAPI;
    private Instant tokenExpiryTime;
    private Object SpringBea;

    @PostConstruct
    private void init() throws Exception {
        authAPI = new AuthAPI(domain, clientId, clientSecret);
        refreshManagementApiToken();
    }

    /* Handle valid token*/
    /*WAY : 1 */
    /*If we want to maintain by scheduler then We can handle like this*/
   /* @Scheduled(fixedRate = 82800000) // 23 hours in milliseconds
    public void refreshManagementApiToken() throws Exception {
        TokenHolder holder = authAPI.requestToken("https://" + domain + "/api/v2/").execute();
        this.managementApiToken = holder.getAccessToken();
    }*/
    /*WAY : 2 */
    /*If we want to Handle by dynamically check with exp time then we can handle like this*/
    public synchronized void refreshManagementApiToken() throws Exception {
        TokenHolder holder = authAPI.requestToken("https://" + domain + "/api/v2/").execute();
        this.managementApiToken = holder.getAccessToken();
        this.tokenExpiryTime = Instant.now().plusSeconds(holder.getExpiresIn());
    }

    public synchronized String getManagementApiToken() throws Exception {
        if (Instant.now().isAfter(tokenExpiryTime)) {
            refreshManagementApiToken();
        }
        logger.info("Refreshing Management API token");
        return managementApiToken;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ManagementAPI managementAPI() throws Exception {
        logger.info("Creating a new ManagementAPI instance with token: {}", managementApiToken);
        return new ManagementAPI(domain, getManagementApiToken());
    }
}