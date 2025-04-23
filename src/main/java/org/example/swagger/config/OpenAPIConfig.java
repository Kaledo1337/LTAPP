package org.example.swagger.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * This class is a configuration class for OpenAPI.
 * It generates and returns the OpenAPI object for the Tutorial Management API.
 */
@Configuration
public class OpenAPIConfig {


    /**
     * The devUrl variable represents the URL of the development server in the OpenAPIConfig class.
     * It is retrieved from the application properties using the value of the "xset.openapi.dev-url" key.
     */
    @Value("${xset.openapi.dev-url}")
    private String devUrl;

    /**
     * The prodUrl variable represents the URL of the production server in the OpenAPIConfig class.
     * It is retrieved from the application properties using the value of the "xset.openapi.prod-url" key.
     */
    @Value("${xset.openapi.prod-url}")
    private String prodUrl;

    /**
     * Generates and returns the OpenAPI object for the Tutorial Management API.
     *
     * @return the OpenAPI object containing the API information
     */
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("xset@gmail.com");
        contact.setName("XSet");
        contact.setUrl("https://www.xset.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Tutorial Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.").termsOfService("https://www.xset.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}