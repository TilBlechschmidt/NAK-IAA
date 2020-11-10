package de.nordakademie.iaa.noodle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main entry point of the noodle application.
 *
 * @author Hans Rißer
 */
@SpringBootApplication
@EnableSwagger2
public class NoodleApplication {
    /**
     * Starts the noodle application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(NoodleApplication.class, args);
    }
}
