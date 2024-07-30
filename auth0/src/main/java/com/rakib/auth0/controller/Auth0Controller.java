package com.rakib.auth0.controller;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.organizations.Organization;
import com.auth0.json.mgmt.users.User;
import com.rakib.auth0.model.AccessTokenRequest;
import com.rakib.auth0.service.Auth0ManagementService;
import com.rakib.auth0.service.Auth0ManagementServiceImpl;
import com.rakib.auth0.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth0")
@RequiredArgsConstructor
public class Auth0Controller {

    private final Auth0ManagementService auth0ManagementService;
    private final AuthUtils authUtils;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = auth0ManagementService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/organizations")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        try {
            Organization createdOrganization = auth0ManagementService.createOrganization(organization);
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
}
