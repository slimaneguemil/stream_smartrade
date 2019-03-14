package com.mks;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RunLfx2Poll {


    public static void main(String args[]){
        SpringApplication.run(RunLfx2Poll.class,args);
    }


}
