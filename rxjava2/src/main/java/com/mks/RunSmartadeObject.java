package com.mks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.converter.MessageConverter;

import java.io.IOException;


@SpringBootApplication
public class RunSmartadeObject {
    Log log = LogFactory.getLog(getClass());
    public static void main(String args[]) {
        SpringApplication.run(RunSmartadeObject.class, args);
    }

    // Loadbalancing : consumer  1 by subscribing
    //@Bean
    String broadcast2_old(ConsumerChannels channels) {
        channels.broadcasts().subscribe(message -> {
            String messagePayload = (String) message.getPayload();
            log.info("message converter : {}"+ messagePayload);
        });
        return null;
    }

    @Bean
    String broadcast2(ConsumerChannels channels) {
        channels.broadcasts().subscribe(
                new MessageHandler() {
                    @Override
                    public void handleMessage(Message<?> message) throws MessagingException {
                        Object payload= message.getPayload();
                        String jsonString = payload.toString();

                        ObjectMapper objectMapper = new ObjectMapper();
                        Foo foo = null;
                        try {
                            foo = objectMapper.readValue(jsonString, Foo.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        log.info(foo);
                        if (payload instanceof Foo)
                        log.info("*** message is Foo");
                        else
                            log.info("*** message not a foo");
                    }
                }
        );
        return null;
    }


    // Loadbalancing : consumer  2 by reactive streaming
   // @StreamListener(ConsumerChannels.BROADCASTS)
    public void onBroadcastListenining(String message) {
       log.info("(grp1- loadbalanced)  consumer 2 - by reactive stream  :"+ message);
    }


    // Loadbalancing : consumer  3 by polling
    // a polled consumer lets application control message proessing
    //@Bean
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


    //parallel consumption
    @StreamListener(ConsumerChannels.DIRECTED)
    public void onNewDirectedlistening(Foo message) {
        log.info("(grp2 - parallel) consumer 1 - by reactive stream <Object>:" + message);
    }

   // @StreamListener(ConsumerChannels.DIRECTED)
    public void onNewDirectedListening2(String message) {
        log.info("(grp2 - parallel)  consumer 2 - by reactive stream <String>:" + message);
    }



}