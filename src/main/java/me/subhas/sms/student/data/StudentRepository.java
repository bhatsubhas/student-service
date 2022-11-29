package me.subhas.sms.student.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import me.subhas.sms.student.data.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s from Student s WHERE s.email = ?1")
    Optional<Student> findByStudentEmail(String email);
}
