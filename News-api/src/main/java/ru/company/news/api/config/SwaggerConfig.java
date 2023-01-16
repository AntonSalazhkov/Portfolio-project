package ru.company.news.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger configuration class.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Definition of the scope.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.company.news.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
