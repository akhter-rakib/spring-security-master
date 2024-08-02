package com.rakib.auth0.model;

import lombok.Data;

@Data
public class CreateOrganizationAndUserRequest {
    private String organizationName;
    private String organizationDisplayName;
    private String userEmail;
    private String userName;
    private char[] password;
}
