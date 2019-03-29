package com.mks.config;

import com.mks.BusClientInterface;
import com.mks.BusClientRxJava;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan("com.mks")
public class TestConfig {

    @Bean("testBus")
    BusClientInterface busClientRxJava(){
        return new BusClientRxJava();
    }

}
