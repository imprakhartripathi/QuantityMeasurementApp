package com.imprakhartripathi.qmaserver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "QMA Auth API",
        version = "v1",
        description = "Spring Boot REST API for authentication and user profile"))
public class QmaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QmaServerApplication.class, args);
    }

}
