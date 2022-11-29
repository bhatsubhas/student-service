package me.subhas.sms.student.business.exception;

public class StudentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -359151207510962606L;

    public StudentNotFoundException(String message) {
	super(message);
    }

}
