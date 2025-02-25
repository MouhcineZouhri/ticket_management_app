package org.example.models;

import lombok.*;
import org.example.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
