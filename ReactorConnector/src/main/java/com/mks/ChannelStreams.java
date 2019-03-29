package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding({ProducerChannels.class, ConsumerChannels.class})
public class ChannelStreams {
    Log log = LogFactory.getLog(getClass());
    ChannelServiceRxJava channelServiceRxJava;
    ChannelServiceReactor channelServiceReactor;


    ChannelStreams(ChannelServiceRxJava channelServiceRxJava, ChannelServiceReactor channelServiceReactor){
        this.channelServiceRxJava = channelServiceRxJava;
        this.channelServiceReactor = channelServiceReactor;
    }


    @StreamListener(ConsumerChannels.INPUTBROADCASTS)
    private void subscribeBroadcast(Foo message) {
        log.info("@StreamListener for BROADCAST received:" + message);
        this.channelServiceRxJava.getSubject().onNext(message);
        if (this.channelServiceReactor.webSocketCommentSink != null) {
            log.info("Publishing " + message.toString() +
                    " to websocket...");
            this.channelServiceReactor.webSocketCommentSink.next(message);
        }
    }




}