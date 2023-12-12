package com.desbravador.desafioJava.exceptionhandler;

import com.desbravador.desafioJava.exceptionhandler.exception.NotFoundException;
import com.desbravador.desafioJava.exceptionhandler.exception.ValidateException;
import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Provider
public class ExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Throwable > {

	@Override
	public Response toResponse(Throwable throwable) {
		Log.error("Exception caught: " + throwable.getMessage(), throwable);

		if (throwable instanceof NotFoundException) {
			return handleNotFoundException((NotFoundException) throwable);
		}
		if (throwable instanceof ValidateException) {
			return handleValidateException((ValidateException) throwable);
		} else {
			Error error = Error.builder()
					.dateTime(LocalDateTime.now())
					.message("Erro interno do servidor")
					.build();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(error)
					.build();
		}
	}

	private Response handleNotFoundException(NotFoundException e) {
		var error = Error.builder()
				.dateTime(LocalDateTime.now())
				.message(e.getMessage()).build();

		return Response.status(Response.Status.NOT_FOUND)
				.entity(error)
				.build();
	}

	private Response handleValidateException(ValidateException e) {
		var error = Error.builder()
				.dateTime(LocalDateTime.now())
				.message(e.getMessage()).build();

		return  Response.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.entity(error)
				.build();
	}
}