package com.rakib.auth0.controller;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.users.User;
import com.rakib.auth0.model.AccessTokenRequest;
import com.rakib.auth0.model.CreateOrganizationAndUserRequest;
import com.rakib.auth0.model.CreateOrganizationRequest;
import com.rakib.auth0.model.CreateUserRequest;
import com.rakib.auth0.model.OktaUser;
import com.rakib.auth0.service.Auth0ManagementService;
import com.rakib.auth0.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth0")
@RequiredArgsConstructor
public class Auth0Controller {

    private final Auth0ManagementService auth0ManagementService;
    private final AuthUtils authUtils;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            User createdUser = auth0ManagementService.createUser(createUserRequest);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/organizations")
    public ResponseEntity<Organization> createOrganization(@Valid @RequestBody CreateOrganizationRequest organizationRequest) {
        try {
            Organization createdOrganization = auth0ManagementService.createOrganization(organizationRequest);
            return ResponseEntity.ok(createdOrganization);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user/info")
    public User getUserInfoByToken(@RequestBody AccessTokenRequest accessTokenRequest) throws Auth0Exception {
        return auth0ManagementService.getUserInfoByToken(accessTokenRequest.getToken());
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyToken(@RequestBody String token) {
        try {
            authUtils.verifyToken(token);
            return ResponseEntity.ok("Token is valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid: " + e.getMessage());
        }
    }

    @PostMapping("/organizations/{organizationId}/users/{userId}")
    public ResponseEntity<String> addUserToOrganization(
            @PathVariable String organizationId,
            @PathVariable String userId) {
        try {
            auth0ManagementService.addUserToOrganization(organizationId, userId);
            return ResponseEntity.ok("User added to organization successfully");
        } catch (Auth0Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add user to organization: " + e.getMessage());
        }
    }

    @PostMapping("/organizations-and-users")
    public ResponseEntity<String> createOrganizationAndUser(@Valid @RequestBody CreateOrganizationAndUserRequest request) {
        try {
            auth0ManagementService.createOrganizationAndUser(request);
            return ResponseEntity.ok("Organization and user created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create organization and user: " + e.getMessage());
        }
    }
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeOrganizations(@RequestBody List<String> organizationIds) {
        try {
            auth0ManagementService.removeOrganizations(organizationIds);
            return ResponseEntity.ok("Organizations successfully removed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing organizations: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<OktaUser>> getAllUsers() {
        try {
            List<OktaUser> users = auth0ManagementService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Auth0Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
