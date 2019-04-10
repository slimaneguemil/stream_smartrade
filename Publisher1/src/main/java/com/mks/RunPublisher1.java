package com.mks;

import com.mks.utils.Deal;

import com.mks.utils.utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;


import java.util.concurrent.atomic.AtomicLong;

@EnableBinding(Source.class)
@SpringBootApplication
public class RunPublisher1 {

    public static void main(String[] args) {
        SpringApplication.run(RunPublisher1.class, args);
    }
    AtomicLong count= new AtomicLong(0);


    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "3000"))
    public Deal timeemitter2() {
        Deal m = utils.getDeal(

                count.incrementAndGet(),"from @InnboundAdaptater",
                System.currentTimeMillis());

        utils.log("@InboundChannelAdapter pushing foo = " + m);
        return m;
    }



}