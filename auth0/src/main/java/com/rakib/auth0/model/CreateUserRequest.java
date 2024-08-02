package com.rakib.auth0.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {
    private String email;
    private String name;
    private char[] password;
}