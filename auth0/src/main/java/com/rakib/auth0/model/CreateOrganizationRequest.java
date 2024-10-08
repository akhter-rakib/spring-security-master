package com.rakib.auth0.model;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrganizationRequest {
    /*In auth0 organization name can only contain lowercase alphanumeric characters, hyphens, and underscores */
    @Pattern(regexp = "^[a-z0-9_-]+$", message = "Name can only contain lowercase alphanumeric characters, hyphens, and underscores")
    private String name;
    private String displayName;
}
