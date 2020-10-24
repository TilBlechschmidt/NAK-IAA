package de.nordakademie.iaa.noodle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class NoodleApplication {
	public static void main(String[] args) {
		SpringApplication.run(NoodleApplication.class, args);
	}
}
