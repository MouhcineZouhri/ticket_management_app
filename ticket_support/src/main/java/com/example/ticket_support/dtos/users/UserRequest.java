package com.example.ticket_support.dtos.users;

import com.example.ticket_support.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserRequest {
    private String name;
    private String email;
    private String password;
    @JsonProperty("confirm_password")
    private String confirmPassword;
    private Role role;
}
