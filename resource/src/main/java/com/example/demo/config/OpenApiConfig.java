package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

   @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Votações")
                        .description("API SpringBoot de cadastro de Eleições, Candidatos e Votos")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Arthur Azevedo")
                                .url("https://github.com/arthuraz-1990")
                                .email("arthur.azevedo@accenture.com"))

                );
    }
}
