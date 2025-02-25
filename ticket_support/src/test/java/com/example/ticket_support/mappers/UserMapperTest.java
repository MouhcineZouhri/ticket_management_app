package com.example.ticket_support.mappers;

import com.example.ticket_support.dtos.users.UserRequest;
import com.example.ticket_support.dtos.users.UserResponse;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    public void beforeEach(){
        userMapper = new UserMapperManual();
    }

    @Test
    void userResponseFromUser_ShouldMapCorrectly() {
        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("mouhcine@example.com");
        user.setName("mouhcine");
        user.setRole(Role.EMPLOYEE);

        // when
        UserResponse response = userMapper.userResponseFromUser(user);

        // then
        assertThat(response)
                .isNotNull();

        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getRole()).isEqualTo(user.getRole());
    }

    @Test
    void userFromUserRequest_ShouldMapCorrectly(){
        // given
        UserRequest userRequest = UserRequest.builder()
                .name("mouhcine")
                .email("mouhcine")
                .password("password")
                .confirmPassword("password")
                .role(Role.EMPLOYEE)
                .build();

        // when
        User response = userMapper.userFromUserRequest(userRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(userRequest.getEmail());
        assertThat(response.getName()).isEqualTo(userRequest.getName());
        assertThat(response.getPassword()).isEqualTo(userRequest.getPassword());
        assertThat(response.getRole()).isEqualTo(userRequest.getRole());
    }
}