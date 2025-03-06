package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.enums.Role;
import org.example.models.User;

// one instance initialise for all application contains information about login user.
public class UserHolder {

    private final User user;

    private static UserHolder INSTANCE = new UserHolder(null);

    private UserHolder(User user) {
        this.user = user;
    }

    public static UserHolder getInstance() {
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        INSTANCE = new UserHolder(user);
    }

    public void setUser(JsonNode node) {
        User user = User.builder()
                .id(Long.parseLong(node.get("id").toString()))
                .name(node.get("name").toString())
                .email(node.get("email").toString())
                .role(Role.valueOf(node.get("role").toString().replace("\"", "")))
                .build();
        INSTANCE = new UserHolder(user);
    }

}
