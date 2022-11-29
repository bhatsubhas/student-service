package me.subhas.sms.student.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import me.subhas.sms.student.business.exception.EmailAlreadyTakenException;
import me.subhas.sms.student.business.exception.StudentNotFoundException;

@ControllerAdvice
public class StudentExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StudentNotFoundException.class)
    ErrorResponse exceptionHander(StudentNotFoundException ex) {
	return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyTakenException.class)
    ErrorResponse exceptionHander(EmailAlreadyTakenException ex) {
	return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

}
