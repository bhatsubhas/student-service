package me.subhas.sms.student.business.exception;

public class EmailAlreadyTakenException extends RuntimeException {
    private static final long serialVersionUID = -2992648937630414438L;

    public EmailAlreadyTakenException(String message) {
	super(message);
    }

}
