package com.rakib.auth0.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OktaUser {
    private String id;
    private String name;
    private String email;
}
