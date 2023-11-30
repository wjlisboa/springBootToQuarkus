package com.desbravador.desafioJava.config;

import io.smallrye.openapi.api.models.OpenAPIImpl;
import io.smallrye.openapi.api.models.info.ContactImpl;
import io.smallrye.openapi.api.models.info.InfoImpl;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.info.Contact;
import org.eclipse.microprofile.openapi.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI myOpenAPI() {

    Contact contact = new ContactImpl();
    contact.setEmail("desafio@desbravador.com.br");
    contact.setName("Desafio Java");
    contact.setUrl("http://desbravador.com.br/");

    Info info =
        new InfoImpl()
            .title("Desbravador - Desafio Java")
            .version("1.0")
            .contact(contact)
            .description("Api para prover funcionalidades do desafio Java.");

    return new OpenAPIImpl().info(info);
  }
}
