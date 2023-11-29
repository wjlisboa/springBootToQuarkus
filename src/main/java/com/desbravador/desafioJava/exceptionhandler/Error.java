package com.desbravador.desafioJava.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Error {

	private LocalDateTime dateTime;
	private String message;
	
}
