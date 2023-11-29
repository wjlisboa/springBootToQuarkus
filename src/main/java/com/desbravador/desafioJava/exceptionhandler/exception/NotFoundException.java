package com.desbravador.desafioJava.exceptionhandler.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }
}
