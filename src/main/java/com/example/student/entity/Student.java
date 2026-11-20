package com.example.student.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Course is required")
    private String course;

    @Min(value = 15, message = "Age must be at least 15")
    @Max(value = 100, message = "Age must be at most 100")
    private int age;

    public Student() {
    }

    public Student(Long id, String name, String email, String course, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.age = age;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}