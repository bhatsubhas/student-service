package me.subhas.sms.student.business;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import me.subhas.sms.student.business.exception.EmailAlreadyTakenException;
import me.subhas.sms.student.business.exception.StudentNotFoundException;
import me.subhas.sms.student.data.StudentRepository;
import me.subhas.sms.student.data.entity.Student;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
	this.studentRepository = studentRepository;
    }

    public List<Student> listStudents() {
	return studentRepository.findAll();
    }

    public Student getStudent(Long studentId) {
	return studentRepository.findById(studentId).orElseThrow(
		() -> new StudentNotFoundException(String.format("Student with id %d does not exist", studentId)));
    }

    public void createStudent(Student student) {
	requireUniqueEmail(student.getEmail());
	studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
	boolean exists = studentRepository.existsById(studentId);
	if (!exists) {
	    throw new StudentNotFoundException(String.format("Student with id %d does not exist", studentId));
	}
	studentRepository.deleteById(studentId);

    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
	Student student = getStudent(studentId);
	if (isValidValue(name, student.getName())) {
	    student.setName(name);
	}
	if (isValidValue(email, student.getEmail())) {
	    requireUniqueEmail(email);
	    student.setEmail(email);
	}
    }

    private boolean isValidValue(String newValue, String currentValue) {
	return newValue != null && !newValue.isBlank() && !Objects.equals(newValue, currentValue);
    }

    private void requireUniqueEmail(String email) {
	Optional<Student> studentOptional = studentRepository.findByStudentEmail(email);
	if (studentOptional.isPresent()) {
	    throw new EmailAlreadyTakenException("Email is already taken");
	}
    }
}
