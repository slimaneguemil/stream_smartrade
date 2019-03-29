package com.mks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootApplication
public class RunMe {

    public static void main(String args[]) {
        SpringApplication.run(RunMe.class, args);

    }
}
