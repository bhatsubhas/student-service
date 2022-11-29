package me.subhas.sms.student.data.entity;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table
public class Student {
    @Id
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    @Transient
    private Integer age;

    public Student() {
    }

    public Student(String name, String email, LocalDate dob) {
	this.name = name;
	this.email = email;
	this.dob = dob;
    }

    public Student(Long id, String name, String email, LocalDate dob) {
	this.id = id;
	this.name = name;
	this.email = email;
	this.dob = dob;
    }

    public Long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getEmail() {
	return email;
    }

    public LocalDate getDob() {
	return dob;
    }

    public Integer getAge() {
	return Period.between(dob, LocalDate.now()).getYears();
    }

    public void setId(Long id) {
	this.id = id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public void setDob(LocalDate dob) {
	this.dob = dob;
    }

    public void setAge(Integer age) {
	this.age = age;
    }

    @Override
    public String toString() {
	return String.format("Student [id=%s, name=%s, email=%s, dob=%s, age=%s]", id, name, email, dob, age);
    }

}
