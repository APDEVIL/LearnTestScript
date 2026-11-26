package com.example.student.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Role {
    USER(Set.of(Permission.STUDENT_READ)),
    ADMIN(Set.of(Permission.STUDENT_READ, Permission.STUDENT_CREATE,
            Permission.STUDENT_UPDATE, Permission.STUDENT_DELETE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getValue())));
        return authorities;
    }
}