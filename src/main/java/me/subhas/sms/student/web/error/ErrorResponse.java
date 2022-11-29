package me.subhas.sms.student.web.error;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private int statusCode;
    private String errorMessage;

    public ErrorResponse(HttpStatus status, String message) {
	this.statusCode = status.value();
	this.errorMessage = message;
    }

    public int getStatusCode() {
	return statusCode;
    }

    public String getErrorMessage() {
	return errorMessage;
    }
}
