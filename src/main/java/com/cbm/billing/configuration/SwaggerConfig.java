package com.cbm.billing.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    /**
     * Customize the OpenAPI configuration for this API.
     * This bean must be defined so that the information in the OpenAPI
     * document is customized for this API. The title, description, version,
     * and license are all set here.
     * @return the customized OpenAPI bean
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Billing System")
                        .description("This is a REST API that performs transactions on accounts and bills and generates reports.")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
