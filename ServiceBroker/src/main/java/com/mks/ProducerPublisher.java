package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding({ProducerChannels.class, ConsumerChannels.class})
public class ProducerPublisher {

    ChannelServiceRxJava channelServiceRxJava;
    ChannelServiceReactor channelServiceReactor;

    public ProducerPublisher(ChannelServiceRxJava channelServiceRxJava, ChannelServiceReactor channelServiceReactor) {
        this.channelServiceRxJava = channelServiceRxJava;
        this.channelServiceReactor = channelServiceReactor;
    }

    Log log = LogFactory.getLog(getClass());
    @StreamListener(ConsumerChannels.DIRECTED)
    private void subscribeDirect(Foo message) {
        log.info("extends class:" + message);
//        if (webSocketCommentSink != null) {
//            log.info("Publishing " + message.toString() +
//                    " to websocket...");
//            webSocketCommentSink.next(message);
//        }
        channelServiceRxJava.subject.onNext(message);
    }

    @StreamListener(ConsumerChannels.BROADCASTS)
    private void subscribeBroadcast(Foo message) {
        log.info("extends class:" + message);
        channelServiceRxJava.subject.onNext(message);

    }
}
