package com.example.student.controller;

import com.example.student.entity.Student;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService service;

    @InjectMocks
    private StudentController controller;

    private final Student student = new Student(1L, "Akash", "akash@test.com", "Java", 22);

    @Test
    void getAll_returns200WithList() {
        when(service.getAllStudents()).thenReturn(List.of(student));

        ResponseEntity<List<Student>> response = controller.getAll();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getById_found_returns200() {
        when(service.getStudentById(1L)).thenReturn(student);

        ResponseEntity<Student> response = controller.getById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getName()).isEqualTo("Akash");
    }

    @Test
    void getById_notFound_throwsException() {
        when(service.getStudentById(99L)).thenThrow(new ResourceNotFoundException("not found"));

        assertThatThrownBy(() -> controller.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_returns201() {
        when(service.createStudent(student)).thenReturn(student);

        ResponseEntity<Student> response = controller.create(student);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void create_duplicateEmail_throwsException() {
        when(service.createStudent(student)).thenThrow(new IllegalArgumentException("duplicate"));

        assertThatThrownBy(() -> controller.create(student))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_returns200() {
        when(service.updateStudent(1L, student)).thenReturn(student);

        ResponseEntity<Student> response = controller.update(1L, student);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void delete_returns204() {
        ResponseEntity<Void> response = controller.delete(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(service).deleteStudent(1L);
    }
}