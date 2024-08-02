package com.rakib.auth0.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.ConnectionFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.Connection;
import com.auth0.json.mgmt.organizations.Members;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import com.rakib.auth0.model.CreateOrganizationAndUserRequest;
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
import java.util.List;


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
    public User createUser(User user) throws Exception {
        user.setConnection(USERNAME_PASSWORD_AUTHENTICATION);
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

    @Override
    public void addUserToOrganization(String organizationId, String userId) throws Auth0Exception {
        Members members = new Members(Collections.singletonList(userId));
        managementAPI().organizations().addMembers(organizationId, members).execute();
    }

    public String getConnectionIdByName(String connectionName) throws Auth0Exception {
        Request<List<Connection>> request = managementAPI().connections().list(new ConnectionFilter());
        List<Connection> connections = request.execute();
        for (Connection connection : connections) {
            if (connection.getName().equals(connectionName)) {
                return connection.getId();
            }
        }
        throw new Auth0Exception("Connection not found: " + connectionName);
    }

    @Override
    public void createOrganizationAndUser(CreateOrganizationAndUserRequest request) throws Exception {
        // Create the organization
        Organization organization = new Organization();
        organization.setName(request.getOrganizationName());
        organization.setDisplayName(request.getOrganizationDisplayName());
        Organization createdOrganization = createOrganization(organization);

        // Create the user
        User user = new User();
        user.setEmail(request.getUserEmail());
        user.setName(request.getUserName());
        user.setPassword(request.getPassword());
        User createdUser = createUser(user);

        // Add the user to the organization
        addUserToOrganization(createdOrganization.getId(), createdUser.getId());
    }
}


