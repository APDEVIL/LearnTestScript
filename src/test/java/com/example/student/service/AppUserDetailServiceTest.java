package com.example.student.service;

import com.example.student.entity.AppUser;
import com.example.student.entity.Role;
import com.example.student.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Test
    void loadUserByUsername_whenUserExists_returnsUserWithRole() {
        AppUser admin = new AppUser("admin@test.com", "hashed-password", Role.ADMIN);
        when(appUserRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));

        UserDetails result = appUserDetailsService.loadUserByUsername("admin@test.com");

        assertEquals("admin@test.com", result.getUsername());
        assertEquals("hashed-password", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_whenNormalUser_hasOnlyUserRole() {
        AppUser user = new AppUser("user@test.com", "hashed-password", Role.USER);
        when(appUserRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        UserDetails result = appUserDetailsService.loadUserByUsername("user@test.com");

        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertFalse(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_whenUserNotFound_throwsException() {
        when(appUserRepository.findByEmail("nobody@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> appUserDetailsService.loadUserByUsername("nobody@test.com"));
    }
}