package com.rakib.auth0.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateOrganizationAndUserRequest {
    @Pattern(regexp = "^[a-z0-9_-]+$", message = "Name can only contain lowercase alphanumeric characters, hyphens, and underscores")
    private String organizationName;
    private String organizationDisplayName;
    private String userEmail;
    private String userName;
    private char[] password;
}
