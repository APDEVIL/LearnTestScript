package com.example.student.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void admin_hasAllStudentPermissions() {
        assertThat(Role.ADMIN.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_ADMIN", "student:read",
                        "student:create", "student:update", "student:delete");
    }

    @Test
    void user_canOnlyRead() {
        assertThat(Role.USER.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_USER", "student:read");
    }
}