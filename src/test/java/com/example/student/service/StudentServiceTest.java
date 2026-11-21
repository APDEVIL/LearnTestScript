package com.example.student.service;

import com.example.student.entity.Student;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    // CREATE 

    @Test
    void createStudent_whenEmailIsNew_savesAndReturnsStudent() {
        Student input = new Student(null, "Akash", "akash@test.com", "Java", 22);
        Student saved = new Student(1L, "Akash", "akash@test.com", "Java", 22);

        when(studentRepository.existsByEmail("akash@test.com")).thenReturn(false);
        when(studentRepository.save(input)).thenReturn(saved);

        Student result = studentService.createStudent(input);

        assertEquals(1L, result.getId());
        assertEquals("Akash", result.getName());
        verify(studentRepository).save(input);
    }

    @Test
    void createStudent_whenEmailAlreadyExists_throwsException() {
        Student input = new Student(null, "Akash", "akash@test.com", "Java", 22);
        when(studentRepository.existsByEmail("akash@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> studentService.createStudent(input));

        assertEquals("Email already exists: akash@test.com", ex.getMessage());
        verify(studentRepository, never()).save(any());
    }

    // READ ALL 

    @Test
    void getAllStudents_returnsList() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Akash", "akash@test.com", "Java", 22),
                new Student(2L, "Ravi", "ravi@test.com", "Python", 21));
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("Akash", result.get(0).getName());
    }

    @Test
    void getAllStudents_whenNoStudents_returnsEmptyList() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> result = studentService.getAllStudents();

        assertTrue(result.isEmpty());
    }

    // READ ONE 

    @Test
    void getStudentById_whenFound_returnsStudent() {
        Student student = new Student(1L, "Akash", "akash@test.com", "Java", 22);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertEquals("Akash", result.getName());
    }

    @Test
    void getStudentById_whenNotFound_throwsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> studentService.getStudentById(99L));

        assertEquals("Student not found with id: 99", ex.getMessage());
    }

    // UPDATE 

    @Test
    void updateStudent_whenFound_updatesAndSaves() {
        Student existing = new Student(1L, "Akash", "akash@test.com", "Java", 22);
        Student updated = new Student(null, "Akash K", "akash@test.com", "Spring Boot", 23);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student result = studentService.updateStudent(1L, updated);

        assertEquals("Akash K", result.getName());
        assertEquals("Spring Boot", result.getCourse());
        assertEquals(23, result.getAge());
        verify(studentRepository).save(existing);
    }

    @Test
    void updateStudent_whenNotFound_throwsException() {
        Student updated = new Student(null, "Akash K", "akash@test.com", "Spring Boot", 23);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> studentService.updateStudent(99L, updated));

        verify(studentRepository, never()).save(any());
    }

    // DELETE 

    @Test
    void deleteStudent_whenFound_deletesStudent() {
        Student existing = new Student(1L, "Akash", "akash@test.com", "Java", 22);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(existing);
    }

    @Test
    void deleteStudent_whenNotFound_throwsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> studentService.deleteStudent(99L));

        verify(studentRepository, never()).delete(any());
    }
}