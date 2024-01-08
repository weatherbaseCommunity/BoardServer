package com.example.makeboard0629;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MakeBoard0629Application {

    public static void main(String[] args) {
        SpringApplication.run(MakeBoard0629Application.class, args);
    }

}
