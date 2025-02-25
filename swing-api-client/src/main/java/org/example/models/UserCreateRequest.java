package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    // private Role role;
}
