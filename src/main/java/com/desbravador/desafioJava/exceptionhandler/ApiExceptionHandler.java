package com.desbravador.desafioJava.exceptionhandler;

import com.desbravador.desafioJava.exceptionhandler.exception.NotFoundException;
import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
		Error error = Error.builder()
				.dateTime(LocalDateTime.now())
				.message(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(error);
	}

	@ExceptionHandler(ValidateException.class)
	public ResponseEntity<?> handleValidateException(ValidateException e) {
		Error error = Error.builder()
				.dateTime(LocalDateTime.now())
				.message(e.getMessage()).build();

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(error);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		Error error = Error.builder()
				.dateTime(LocalDateTime.now())
				.message("Erro não tratado").build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(error);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		String errorMessages = ex.getBindingResult()
									.getFieldErrors().stream()
									.map(DefaultMessageSourceResolvable::getDefaultMessage)
									.collect(Collectors.joining());

		Error error = Error.builder()
				.dateTime(LocalDateTime.now())
				.message(errorMessages).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}


}
