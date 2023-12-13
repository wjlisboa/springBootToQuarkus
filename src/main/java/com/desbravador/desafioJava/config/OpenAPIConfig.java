package com.desbravador.desafioJava.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title="SpringBoot To Quarkus",
                version = "1.0.0",
                description = "Api para prover funcionalidades do desafio Java.",
                contact = @Contact(
                        name = "Quarkus",
                        url = "https://quarkus.io/",
                        email = "desafio@springQuarkus.com"))
)
public class OpenAPIConfig extends Application {
}