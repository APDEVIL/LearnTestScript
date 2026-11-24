package com.example.student.security;

import com.example.student.entity.AppUser;
import com.example.student.entity.Role;
import com.example.student.repository.AppUserRepository;
import com.example.student.service.AppUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordLoginSecurityTest {

    @Mock
    private AppUserRepository repo;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private AuthenticationManager manager;

    @BeforeEach
    void setUp() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(new AppUserDetailsService(repo));
        provider.setPasswordEncoder(encoder);
        manager = new ProviderManager(provider);
    }

    private Authentication login(String password) {
        return manager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated("admin@test.com", password));
    }

    @Test
    void correctPassword_authenticates() {
        when(repo.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(new AppUser("admin@test.com", encoder.encode("admin123"), Role.ADMIN)));

        assertThat(login("admin123").isAuthenticated()).isTrue();
    }

    @Test
    void wrongPassword_isRejected() {
        when(repo.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(new AppUser("admin@test.com", encoder.encode("admin123"), Role.ADMIN)));

        assertThatThrownBy(() -> login("wrong")).isInstanceOf(BadCredentialsException.class);
    }
}