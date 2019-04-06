package com.mks;

import com.mks.utils.Message;
import com.mks.utils.utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.MessageBuilder;

import static java.lang.System.currentTimeMillis;

@EnableBinding(Source.class)
@SpringBootApplication
public class RunPublisher1 {

    public static void main(String[] args) {
        SpringApplication.run(RunPublisher1.class, args);
    }

    // @Bean
    //@InboundChannelAdapter(value=Source.OUTPUT, poller = @Poller(fixedDelay="10"))
    public MessageSource<Foo> timeemitter() {
        Foo foo = new Foo();
        foo.setAmount(1);
        foo.setId(1);
        foo.setName("from @InboundchannelAdaptater");
        System.out.println("@InboundChannelAdapter pushing foo = " + foo);
        return () -> MessageBuilder.withPayload(foo).build();
    }

    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "3000"))
    public Message timeemitter2() {
        Message m = utils.getMessage(

                1,"from @InnboundAdaptater",
                System.currentTimeMillis());

        utils.log("@InboundChannelAdapter pushing foo = " + m);
        return m;
    }
}