package com.example.ticket_support.dtos.users;

import com.example.ticket_support.enums.Role;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
