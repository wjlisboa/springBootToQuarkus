package com.desbravador.desafioJava.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI myOpenAPI() {

    Contact contact = new Contact();
    contact.setEmail("desafio@desbravador.com.br");
    contact.setName("Desafio Java");
    contact.setUrl("http://desbravador.com.br/");

    Info info =
        new Info()
            .title("Desbravador - Desafio Java")
            .version("1.0")
            .contact(contact)
            .description("Api para prover funcionalidades do desafio Java.");

    return new OpenAPI().info(info);
  }
}
