package de.nordakademie.iaa.noodle.controller;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Home redirection to OpenAPI api documentation
 *
 * @author Noah Peeters
 */
@Controller
@Api(value = "ApiDocsController")
public class ApiDocsController {
    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui/index.html";
    }

    /**
     * Creates swagger api information.
     *
     * @return Swagger API Information.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Noodle")
            .description("API of the Noodle planner")
            .license("")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("")
            .version("0.1.0")
            .build();
    }

    /**
     * Creates the Swagger bean.
     *
     * @return Swagger Bean.
     */
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("de.nordakademie.iaa.noodle.controller"))
            .build()
            .apiInfo(apiInfo());
    }
}
