package io.nology.eventscreatorbackend.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.jsonwebtoken.ExpiredJwtException;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> handleExpiredToken(ExpiredJwtException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<String> handleIncorrectDate(DateTimeParseException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, Object> handleInvalidData(MethodArgumentNotValidException ex) {
		Map<String, Object> errorMap = new HashMap<>();
		errorMap.put("timestamp", LocalDateTime.now());
	    errorMap.put("status", HttpStatus.BAD_REQUEST.value());
	    errorMap.put("error", "Bad Request");
        errorMap.put("message", ex.getMessage());
        return errorMap;
	}
	
	//JSONParseError
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleJSONParse(HttpMessageNotReadableException ex) {
		System.out.println("invalid argument handler");
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

}