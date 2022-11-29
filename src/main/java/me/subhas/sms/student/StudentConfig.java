package me.subhas.sms.student;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.subhas.sms.student.data.StudentRepository;
import me.subhas.sms.student.data.entity.Student;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
	return args -> {
	    Student abc = new Student("Abc Xyz", "abc.xyz@school.com", LocalDate.of(1997, 12, 10));
	    Student wuv = new Student("Wuv Hij", "wuv.hij@school.com", LocalDate.of(1998, 8, 15));
	    studentRepository.saveAll(List.of(abc, wuv));
	};
    }
}
