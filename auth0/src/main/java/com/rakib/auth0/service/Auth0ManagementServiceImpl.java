package com.rakib.auth0.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.organizations.Members;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import com.rakib.auth0.model.CreateOrganizationAndUserRequest;
import com.rakib.auth0.model.CreateOrganizationRequest;
import com.rakib.auth0.model.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class Auth0ManagementServiceImpl implements Auth0ManagementService {

    private static final String USERNAME_PASSWORD_AUTHENTICATION = "Username-Password-Authentication";

    @Value("${auth0.domain}")
    private String auth0Domain;
    private final RestTemplate restTemplate;

    @Lookup
    public ManagementAPI managementAPI() {
        return null; // Spring will override this method to return the prototype bean (Auth0TokenConfig#managementAPI)
    }

    @Override
    public User createUser(CreateUserRequest userRequest) throws Exception {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setConnection(USERNAME_PASSWORD_AUTHENTICATION);
        Request<User> request = managementAPI().users().create(user);
        return request.execute();
    }

    @Override
    public Organization createOrganization(CreateOrganizationRequest organizationRequest) throws Exception {
        Organization organization = new Organization();
        organization.setName(organizationRequest.getName());
        organization.setDisplayName(organizationRequest.getDisplayName());
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

    @Override
    public void addUserToOrganization(String organizationId, String userId) throws Auth0Exception {
        Members members = new Members(Collections.singletonList(userId));
        managementAPI().organizations().addMembers(organizationId, members).execute();
    }

    @Override
    public void createOrganizationAndUser(CreateOrganizationAndUserRequest request) throws Exception {
        // Create the organization
        CreateOrganizationRequest organizationRequest = CreateOrganizationRequest
                .builder()
                .displayName(request.getOrganizationDisplayName())
                .name(request.getUserName())
                .build();
        Organization createdOrganization = createOrganization(organizationRequest);

        // Create the user
        CreateUserRequest userRequest = CreateUserRequest
                .builder()
                .email(request.getUserEmail())
                .name(request.getUserName())
                .password(request.getPassword())
                .build();
        User createdUser = createUser(userRequest);

        // Add the user to the organization
        addUserToOrganization(createdOrganization.getId(), createdUser.getId());
    }
}


