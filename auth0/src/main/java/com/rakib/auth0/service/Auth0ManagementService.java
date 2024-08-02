package com.rakib.auth0.service;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.users.User;
import com.rakib.auth0.model.CreateOrganizationAndUserRequest;

public interface Auth0ManagementService {

    User createUser(User user) throws Exception;

    Organization createOrganization(Organization organization) throws Exception;

    User getUserInfoByToken(String accessToken) throws Auth0Exception;

    void addUserToOrganization(String organizationId, String userId) throws Auth0Exception;

    void createOrganizationAndUser(CreateOrganizationAndUserRequest request) throws Exception;
}
