package org.example.springdemoweek2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.example.springdemoweek2") //scans manually, dont rely on devtools -> fixed POST requests errors
public class SpringDemoWeek2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoWeek2Application.class, args);
    }
}
