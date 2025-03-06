package org.example.models;

import lombok.Builder;
import lombok.Getter;
import org.example.enums.Role;

@Getter
@Builder
public class UserCreateRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private Role role;
}
