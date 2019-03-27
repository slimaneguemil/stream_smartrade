package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RunSimulatorReactor implements CommandLineRunner {

    @Autowired
    ChannelServiceReactor channelServiceReactor;

    Log log = LogFactory.getLog(getClass());
    //ChannelServiceRxJava channelService;

    public static void main(String args[]) {
        SpringApplication.run(RunSimulatorReactor.class, args);

    }

    public void run(String[] args) {

       channelServiceReactor.flux.subscribe(
               s -> System.out.println("Reactor Subscriber = " + s)
       );
       Foo foo1 = new Foo();
        foo1.setId(100);
        foo1.setName("reactor message 1");
        foo1.setTag("1");
        channelServiceReactor.publish(foo1);

        Foo foo2 = new Foo();
        foo2.setId(100);
        foo2.setName("reactor message 1");
        foo2.setTag("1");
        channelServiceReactor.publish(foo2);

        //s1.unsubscribe();

        Foo foo3 = new Foo();
        foo3.setId(100);
        foo3.setName("reactor message 3");
        foo3.setTag("1");
        channelServiceReactor.publish(foo3);

    }

    @RequestMapping("/publish/{name}")
    String hi(@PathVariable String name) {
        String message = "Hello, " + name + "!";
        //System.out.println("reactor rest received a new message:"+ message);
        Foo foo1 = new Foo();
        foo1.setId(100);
        foo1.setName(message);
        foo1.setTag("1");

        channelServiceReactor.publish(foo1);


        return "//nSuccess";
    }
}


