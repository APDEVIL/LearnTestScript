package com.example.student.repository;

import com.example.student.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository repo;

    @Test
    void existsByEmail_whenSaved_returnsTrue() {
        repo.save(new Student(null, "Akash", "akash@test.com", "Java", 22));

        assertThat(repo.existsByEmail("akash@test.com")).isTrue();
    }

    @Test
    void existsByEmail_whenNotSaved_returnsFalse() {
        assertThat(repo.existsByEmail("nobody@test.com")).isFalse();
    }
}