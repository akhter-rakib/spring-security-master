package com.rakib.auth0.service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public class Auth0ManagementService {

    private static final Logger logger = LoggerFactory.getLogger(Auth0ManagementService.class);

    @Lookup
    public ManagementAPI managementAPI() {
        return null; // Spring will override this method
        // to return the prototype bean (Auth0TokenConfig#managementAPI)
    }

    public User createUser(User user) throws Exception {
        ManagementAPI managementAPI = managementAPI();
        logger.info("Using ManagementAPI new instance: {}", managementAPI);
        Request<User> request = managementAPI.users().create(user);
        return request.execute();
    }

    public Organization createOrganization(Organization organization) throws Exception {
        ManagementAPI managementAPI = managementAPI();
        Request<Organization> request = managementAPI.organizations().create(organization);
        return request.execute();
    }
}





