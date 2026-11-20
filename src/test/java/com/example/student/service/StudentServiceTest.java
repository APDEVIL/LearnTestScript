package com.example.student.service;

import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createStudent_whenEmailIsNew_savesAndReturnsStudent() {
        // Arrange
        Student input = new Student(null, "Akash", "akash@test.com", "Java", 22);
        Student saved = new Student(1L, "Akash", "akash@test.com", "Java", 22);

        when(studentRepository.existsByEmail("akash@test.com")).thenReturn(false);
        when(studentRepository.save(input)).thenReturn(saved);

        // Act
        Student result = studentService.createStudent(input);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Akash", result.getName());
        verify(studentRepository).save(input);
    }

    @Test
    void createStudent_whenEmailAlreadyExists_throwsException() {
        // Arrange
        Student input = new Student(null, "Akash", "akash@test.com", "Java", 22);
        when(studentRepository.existsByEmail("akash@test.com")).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.createStudent(input));

        assertEquals("Email already exists: akash@test.com", ex.getMessage());
        verify(studentRepository, never()).save(any());
    }
}