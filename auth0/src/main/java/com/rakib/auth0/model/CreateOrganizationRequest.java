package com.rakib.auth0.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrganizationRequest {
    private String name;
    private String displayName;
}
