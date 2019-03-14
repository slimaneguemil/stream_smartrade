package com.mks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProducingRest {

    private final MessageChannel broadcast, direct;

    // <2>
    @Autowired
    ProducingRest(ProducerChannels channels) {
        this.broadcast = channels.broadcastGreetings();
        this.direct = channels.directGreetings();
    }

    @RequestMapping("/hi/{name}")
    String hi(@PathVariable String name) {
        String message = "Hello, " + name + "!";

        System.out.println("received from rest :"+ message);
        // <3>
        this.direct.send(MessageBuilder.withPayload("Direct: " + message).build());

        this.broadcast.send(MessageBuilder.withPayload("Broadcast: " + message)
                .build());
        return "//nSuccess";
    }
}

