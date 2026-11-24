package com.example.student.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_withCorrectCredentials_returns200() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "admin@test.com")
                        .param("password", "admin123"))
                .andExpect(status().isOk());
    }

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "admin@test.com")
                        .param("password", "wrong-password"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withUnknownUser_returns401() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "nobody@test.com")
                        .param("password", "whatever"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withoutCsrfToken_returns403() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin@test.com")
                        .param("password", "admin123"))
                .andExpect(status().isForbidden());
    }

    @Test
    void afterLogin_sessionCanReadStudents() throws Exception {
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "user@test.com")
                        .param("password", "user123"))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        mockMvc.perform(get("/api/students").session(session))
                .andExpect(status().isOk());
    }

    @Test
    void afterLoginAsUser_sessionCannotDeleteStudents() throws Exception {
        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "user@test.com")
                        .param("password", "user123"))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/api/students/1")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}