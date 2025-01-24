package com.groom.yummy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String SERVER_URL;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(servers());
    }

    private Info apiInfo() {
        return new Info()
                .title("yummy 명세서")
                .description("api 명세서")
                .version("1.0.0");
    }

    private List<Server> servers() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url(SERVER_URL).description("YUMMY 명세서"));
        return servers;
    }
}
