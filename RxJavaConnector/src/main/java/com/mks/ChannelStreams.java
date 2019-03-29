package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application-broker.properties")
@EnableBinding({ProducerChannels.class, ConsumerChannels.class})
public class ChannelStreams {
    Log log = LogFactory.getLog(getClass());

    ChannelServiceRxJava channelServiceRxJava;


    ChannelStreams(ChannelServiceRxJava channelServiceRxJava){
        this.channelServiceRxJava = channelServiceRxJava;

    }

    @StreamListener(ConsumerChannels.INPUTBROADCASTS)
    private void subscribeBroadcast(Foo message) {
        log.info("@StreamListener for BROADCAST received:" + message);
        this.channelServiceRxJava.getSubject().onNext(message);
    }




}