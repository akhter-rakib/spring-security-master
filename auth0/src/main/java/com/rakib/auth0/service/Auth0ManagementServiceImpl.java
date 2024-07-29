package com.rakib.auth0.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class Auth0ManagementServiceImpl implements Auth0ManagementService {

    @Value("${auth0.domain}")
    private String auth0Domain;
    private static final Logger logger = LoggerFactory.getLogger(Auth0ManagementServiceImpl.class);

    private final RestTemplate restTemplate;

    @Lookup
    public ManagementAPI managementAPI() {
        return null; // Spring will override this method to return the prototype bean (Auth0TokenConfig#managementAPI)
    }

    @Override
    public User createUser(User user) throws Exception {
        Request<User> request = managementAPI().users().create(user);
        return request.execute();
    }

    @Override
    public Organization createOrganization(Organization organization) throws Exception {
        Request<Organization> request = managementAPI().organizations().create(organization);
        return request.execute();
    }

    @Override
    public User getUserInfoByToken(String accessToken) throws Auth0Exception {
        String userInfoEndpoint = "https://" + auth0Domain + "/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<User> response = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, entity, User.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new Auth0Exception(response.getStatusCode().toString());
        }
    }
}


