package com.example.student.repository;

import com.example.student.entity.AppUser;
import com.example.student.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository repo;

    @Test
    void findByEmail_whenSaved_returnsUser() {
        repo.save(new AppUser("admin@test.com", "hash", Role.ADMIN));

        assertThat(repo.findByEmail("admin@test.com"))
                .isPresent()
                .get().extracting(AppUser::getRole).isEqualTo(Role.ADMIN);
    }

    @Test
    void findByEmail_whenMissing_returnsEmpty() {
        assertThat(repo.findByEmail("nobody@test.com")).isEmpty();
    }

    @Test
    void existsByEmail_whenSaved_returnsTrue() {
        repo.save(new AppUser("user@test.com", "hash", Role.USER));

        assertThat(repo.existsByEmail("user@test.com")).isTrue();
    }

    @Test
    void existsByEmail_whenMissing_returnsFalse() {
        assertThat(repo.existsByEmail("nobody@test.com")).isFalse();
    }
}