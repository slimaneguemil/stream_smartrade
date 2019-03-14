package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.SubscribableChannel;


@SpringBootApplication
public class RunSmartade {
    Log log = LogFactory.getLog(getClass());
    public static void main(String args[]) {
        SpringApplication.run(RunSmartade.class, args);
    }

    //******************** Consumer group **********************
    // Loadbalancing : consumer  1 by subscribing
    @Bean
    String broadcast2(ConsumerChannels channels) {
        channels.broadcasts().subscribe(
                message -> {
                    log.info("(grp1- loadbalanced) consumer 1 - by  subscribing  :"+ message );
                }
        );
        return null;
    }

    // Loadbalancing : consumer  2 by reactive streaming
    @StreamListener(ConsumerChannels.BROADCASTS)
    public void onBroadcastListenining(String message) {
       log.info("(grp1- loadbalanced)  consumer 2 - by reactive stream  :"+ message);
    }


    // Loadbalancing : consumer  3 by polling
    // a polled consumer lets application control message proessing
    @Bean
    IntegrationFlow broadcast(ConsumerChannels channels) {
        return incomingMessageFlow(channels.broadcasts(), "broadcast");
    }
    private IntegrationFlow incomingMessageFlow(SubscribableChannel incoming,
                                                String prefix) {

        return IntegrationFlows
                .from(incoming)
                .transform(String.class, String::toUpperCase)
                .handle(
                        String.class,
                        (greeting, headers) -> {
                            log.info("(grp1- loadbalanced) consumer 3 - by functional   (" + prefix + "): "
                                    + greeting);
                            return null;
                        }).get();
    }

    //******************** Consumer group **********************

    //parallel consumption : Cnsumer 1 & 2
    @StreamListener(ConsumerChannels.DIRECTED)
    public void onNewDirectedlistening(String message) {
        log.info("(grp2 - parallel) consumer 1 - by reactive stream:" + message);
    }

    @StreamListener(ConsumerChannels.DIRECTED)
    public void onNewDirectedListening2(String message) {
        log.info("(grp2 - parallel)  consumer 2 - by reactive stream:" + message);
    }



}