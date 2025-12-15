package com.carddemo.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for API documentation.
 * Provides interactive API documentation at /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cardDemoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CardDemo API")
                        .description("Java 21 REST API for CardDemo - Migrated from IBM Enterprise COBOL 6.3")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CardDemo Team")
                                .email("carddemo@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
