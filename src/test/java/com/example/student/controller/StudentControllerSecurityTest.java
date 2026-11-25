package com.example.student.controller;

import com.example.student.config.SecurityConfig;
import com.example.student.entity.Student;
import com.example.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(SecurityConfig.class)
class StudentControllerTest {

    private static final String BODY =
            "{\"name\":\"Akash\",\"email\":\"akash@test.com\",\"course\":\"Java\",\"age\":22}";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService service;

    @Test
    void notLoggedIn_returns401() throws Exception {
        mockMvc.perform(get("/api/students")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_canRead() throws Exception {
        mockMvc.perform(get("/api/students")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_canCreate() throws Exception {
        when(service.createStudent(any())).thenReturn(new Student(1L, "Akash", "akash@test.com", "Java", 22));

        mockMvc.perform(post("/api/students").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_cannotCreate_returns403() throws Exception {
        mockMvc.perform(post("/api/students").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());

        verify(service, never()).createStudent(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_withoutCsrf_returns403() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_cannotDelete_returns403() throws Exception {
        mockMvc.perform(delete("/api/students/1").with(csrf()))
                .andExpect(status().isForbidden());

        verify(service, never()).deleteStudent(any());
    }
}