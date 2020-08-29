package com.suprateam.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
@EnableSwagger2
public class CarApplication {

    public static void main(String[] args) {
        run(CarApplication.class, args);
    }

}
