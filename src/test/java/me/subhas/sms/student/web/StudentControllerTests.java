package me.subhas.sms.student.web;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import me.subhas.sms.student.business.StudentService;
import me.subhas.sms.student.business.exception.EmailAlreadyTakenException;
import me.subhas.sms.student.business.exception.StudentNotFoundException;
import me.subhas.sms.student.data.entity.Student;

@WebMvcTest
class StudentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    @DisplayName("Successfully get empty array")
    void test_listStudents_returnEmptyArray() throws Exception {

	when(studentService.listStudents()).thenReturn(List.of());

	mockMvc.perform(get("/api/v1/students")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)))
		.andDo(print());

	verify(studentService).listStudents();
    }

    @Test
    @DisplayName("Successfully get a Student detail")
    void test_getStudent_success() throws Exception {

	LocalDate dob = LocalDate.of(1987, 8, 1);
	int expectedAge = Period.between(dob, LocalDate.now()).getYears();
	Student student = new Student(23L, "ABC XYZ", "abc.xyz@school.com", dob);
	when(studentService.getStudent(anyLong())).thenReturn(student);

	mockMvc.perform(get("/api/v1/students/23")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(23L))
		.andExpect(jsonPath("$.name").value("ABC XYZ"))
		.andExpect(jsonPath("$.email").value("abc.xyz@school.com"))
		.andExpect(jsonPath("$.dob").value("1987-08-01")).andExpect(jsonPath("$.age").value(expectedAge))
		.andDo(print());

	verify(studentService).getStudent(23L);
    }

    @Test
    @DisplayName("Returns Not Found, when requested student not found while getting")
    void test_getStudent_failure() throws Exception {
	long studentId = 23L;
	when(studentService.getStudent(anyLong()))
		.thenThrow(new StudentNotFoundException(String.format("Student with id %d does not exist", studentId)));

	mockMvc.perform(get("/api/v1/students/23")).andExpect(status().isNotFound())
		.andExpect(jsonPath("$.statusCode").value(404))
		.andExpect(jsonPath("$.errorMessage").value("Student with id 23 does not exist")).andDo(print());

	verify(studentService).getStudent(23L);
    }

    @Test
    @DisplayName("Successfuly create a student")
    void test_createStudent_success() throws Exception {

	StringBuilder jsonStudendBuilder = new StringBuilder();
	jsonStudendBuilder.append("{\n");
	jsonStudendBuilder.append("\"name\": \"Abc Xyz\",\n");
	jsonStudendBuilder.append("\"email\": \"abc.xyz@gmail.com\",\n");
	jsonStudendBuilder.append("\"dob\":\"2000-10-06\"\n");
	jsonStudendBuilder.append("\n}");

	mockMvc.perform(
		post("/api/v1/students").contentType(MediaType.APPLICATION_JSON).content(jsonStudendBuilder.toString()))
		.andExpect(status().isOk()).andDo(print());

	verify(studentService).createStudent(any(Student.class));

    }

    @Test
    @DisplayName("Bad Request when creating student's email id is already taken")
    void test_createStudent_failure() throws Exception {

	doThrow(new EmailAlreadyTakenException("Email is already taken")).when(studentService)
		.createStudent(any(Student.class));
	StringBuilder jsonStudendBuilder = new StringBuilder();
	jsonStudendBuilder.append("{\n");
	jsonStudendBuilder.append("\"name\": \"Xyz Abc\",\n");
	jsonStudendBuilder.append("\"email\": \"xyz.abc@gmail.com\",\n");
	jsonStudendBuilder.append("\"dob\":\"2000-06-10\"\n");
	jsonStudendBuilder.append("\n}");

	mockMvc.perform(
		post("/api/v1/students").contentType(MediaType.APPLICATION_JSON).content(jsonStudendBuilder.toString()))
		.andExpect(status().isBadRequest()).andDo(print());

	verify(studentService).createStudent(any(Student.class));

    }

    @Test
    @DisplayName("Updates existing student with new name & email")
    void test_updateStuent_sucess() throws Exception {
	mockMvc.perform(put("/api/v1/students/13?name=Xyz&email=xyz.a@school.com")).andExpect(status().isOk())
		.andDo(print());
	verify(studentService).updateStudent(13L, "Xyz", "xyz.a@school.com");
    }

    @Test
    @DisplayName("Returns Not Found, when requested student not found for update")
    void test_updateStuent_notFound() throws Exception {
	long udpateStudentId = 13L;
	doThrow(new StudentNotFoundException(String.format("Student with id %d does not exist", udpateStudentId)))
		.when(studentService).updateStudent(anyLong(), anyString(), anyString());
	mockMvc.perform(put("/api/v1/students/13?name=Xyz&email=xyz.abc@school.com"))
		.andExpect(status().isNotFound()).andDo(print());
	verify(studentService).updateStudent(13L, "Xyz", "xyz.abc@school.com");
    }

    @Test
    @DisplayName("Deletes student when it is present")
    void test_deleteStudent_success() throws Exception {
	mockMvc.perform(delete("/api/v1/students/12")).andExpect(status().isOk()).andDo(print());

	verify(studentService).deleteStudent(12L);
    }

    @Test
    @DisplayName("Returns Not Found, when requested student not found for delete")
    void test_deleteStudent_notFound() throws Exception {

	long deleteStudentId = 12L;
	doThrow(new StudentNotFoundException(String.format("Student with id %d does not exist", deleteStudentId)))
		.when(studentService).deleteStudent(deleteStudentId);
	mockMvc.perform(delete("/api/v1/students/12")).andExpect(status().isNotFound()).andDo(print());

	verify(studentService).deleteStudent(deleteStudentId);
    }

}
