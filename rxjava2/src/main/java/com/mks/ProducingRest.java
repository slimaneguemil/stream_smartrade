package com.mks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProducingRest {


    private final ChannelServiceRxJava channelServiceRxJava;

    // <2>
    @Autowired
    ProducingRest(ChannelServiceRxJava channelServiceRxJava) {
       this.channelServiceRxJava = channelServiceRxJava;
    }

    @RequestMapping("/object/{name}")
    String hi(@PathVariable String name) {
        String message = "Hello, " + name + "!";
        System.out.println("received from object rest :"+ message);
        Foo foo1 = new Foo();
        foo1.setId(100);
        foo1.setName(message);
        foo1.setTag("1");

        channelServiceRxJava.publish(foo1);
        //this.direct.send(MessageBuilder.withPayload(foo1).build());
        //this.broadcast.send(MessageBuilder.withPayload(foo1).build());

        return "//nSuccess";
    }
}

