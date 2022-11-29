package me.subhas.sms.student.web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.subhas.sms.student.business.StudentService;
import me.subhas.sms.student.data.entity.Student;

@RestController
@RequestMapping(path = "api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
	this.studentService = studentService;
    }

    @GetMapping
    public List<Student> listStudents() {
	return studentService.listStudents();
    }

    @GetMapping(path = "/{studentId}")
    public Student getStudent(@PathVariable("studentId") Long studentId) {
	return studentService.getStudent(studentId);
    }

    @PostMapping
    public void createStudent(@RequestBody Student student) {
	studentService.createStudent(student);
    }

    @DeleteMapping(path = "/{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
	studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "/{studentId}")
    public void updateStudent(@PathVariable("studentId") Long studentId, @RequestParam(required = false) String name,
	    @RequestParam(required = false) String email) {
	studentService.updateStudent(studentId, name, email);
    }
}
