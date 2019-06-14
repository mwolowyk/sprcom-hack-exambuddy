package com.springer.hack.exambuddy;

import com.springer.hack.exambuddy.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SwaggerConfig.class})
public class ExambuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExambuddyApplication.class, args);
    }

}
