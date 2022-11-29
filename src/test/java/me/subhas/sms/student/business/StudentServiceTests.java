package me.subhas.sms.student.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import me.subhas.sms.student.business.exception.EmailAlreadyTakenException;
import me.subhas.sms.student.business.exception.StudentNotFoundException;
import me.subhas.sms.student.data.StudentRepository;
import me.subhas.sms.student.data.entity.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceTests {

	@Mock
	private StudentRepository studentRepository;

	@Autowired
	@InjectMocks
	private StudentService studentService;

	@Test
    @DisplayName("Return empty list when no students are present")
    void test_listStudents_emptyArray() {
	when(studentRepository.findAll()).thenReturn(List.of());

	List<Student> resultList = studentService.listStudents();
	assertEquals(0, resultList.size());

	verify(studentRepository).findAll();
    }

	@Test
	@DisplayName("Return a list of students")
	void test_listStudents() {
		List<Student> students = List.of(new Student(1L, "XYZ BYZ", "xyz.byz@school.com", LocalDate.of(1999, 10, 1)),
				new Student(2L, "ABC GLZ", "abc.glz@school.com", LocalDate.of(1999, 11, 1)),
				new Student(1L, "HIJ BYZ", "HIJ.byz@school.com", LocalDate.of(1998, 12, 1)));
		when(studentRepository.findAll()).thenReturn(students);

		List<Student> resultList = studentService.listStudents();
		assertEquals(students.size(), resultList.size());

		for (Student student : resultList) {
			int expectedAge = Period.between(student.getDob(), LocalDate.now()).getYears();
			assertEquals(expectedAge, student.getAge());
		}

		verify(studentRepository).findAll();
	}

	@Test
	@DisplayName("Successfully return a student object")
	void test_getStudent_success() {
		Student student = new Student(1L, "ABC GLZ", "abc.glz@school.com", LocalDate.of(1995, 10, 1));
		when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

		Student foundStudent = studentService.getStudent(1L);

		assertEquals(student.getId(), foundStudent.getId());
		int expectedAge = Period.between(student.getDob(), LocalDate.now()).getYears();
		assertEquals(expectedAge, foundStudent.getAge());

		verify(studentRepository).findById(1L);
	}

	@Test
    @DisplayName("Return Not Found,  when student with requested id is not present")
    void test_getStudent_failure() {
	when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

	assertThrows(StudentNotFoundException.class, () -> studentService.getStudent(1L));

	verify(studentRepository).findById(1L);
    }

	@Test
	@DisplayName("Creates a student when all the details are correct")
	void test_createStudent_success() {
		Student newStudent = new Student(1L, "Abc Xyz", "abc.xyz@school.com", LocalDate.of(1997, 7, 10));
		when(studentRepository.save(any(Student.class))).thenReturn(newStudent);
		studentService.createStudent(newStudent);

		verify(studentRepository).save(newStudent);
	}

	@Test
	@DisplayName("Throws exception when creating a Student, if email is already taken")
	void test_createStudent_failure() {
		Student newStudent = new Student("Abc Xyz", "abc.xyz@school.com", LocalDate.of(1997, 7, 10));
		Student existingStudent = new Student(1L, "Abc Xyz", "abc.xyz@school.com", LocalDate.of(1997, 7, 10));
		when(studentRepository.findByStudentEmail(anyString())).thenReturn(Optional.of(existingStudent));
		assertThrows(EmailAlreadyTakenException.class, () -> studentService.createStudent(newStudent));

		verify(studentRepository).findByStudentEmail("abc.xyz@school.com");
	}

	@Test
    @DisplayName("Successfully delete an existing Student")
    void test_deleteStudent_success() {
	when(studentRepository.existsById(anyLong())).thenReturn(true);

	studentService.deleteStudent(123L);

	verify(studentRepository).existsById(123L);
	verify(studentRepository).deleteById(123L);
    }

	@Test
    @DisplayName("Throws exception when deleting Student, if student with specified id is not present")
    void test_deleteStudent_failure() {
	when(studentRepository.existsById(anyLong())).thenReturn(false);

	assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(123L));

	verify(studentRepository).existsById(123L);
    }

	@ParameterizedTest
	@MethodSource("nameAndEmailProvider")
	@DisplayName("Updates existing student's name & email successfully")
	void test_updateStudent_success(String newName, String newEmail) {
		Student student = new Student(23L, "Abc", "abc@school.com", LocalDate.of(1997, 7, 10));
		when(studentRepository.findById(23L)).thenReturn(Optional.of(student));

		studentService.updateStudent(23L, newName, newEmail);

		verify(studentRepository).findById(23L);
	}

	static Stream<Arguments> nameAndEmailProvider() {
		return Stream.of(
				Arguments.arguments("Abc Xyz", "abc.xyz@school.com"),
				Arguments.arguments(null, null),
				Arguments.arguments("", ""),
				Arguments.arguments("Abc", "abc@school.com"));
	}

	@Test
    @DisplayName("Throws exception when updating Student, if student with specified id is not present")
    void test_updateStudent_failure() {
	when(studentRepository.findById(23L)).thenReturn(Optional.empty());

	assertThrows(StudentNotFoundException.class,
		() -> studentService.updateStudent(23L, "Subhas", "subhas.bhat@school.com"));
	verify(studentRepository).findById(23L);
    }

}
