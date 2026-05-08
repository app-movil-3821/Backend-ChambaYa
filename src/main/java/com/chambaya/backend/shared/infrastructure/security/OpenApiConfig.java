package com.chambaya.backend.shared.infrastructure.security;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("/");
        server.setDescription("Current server");
        return new OpenAPI()
                .servers(List.of(server));
    }

}
