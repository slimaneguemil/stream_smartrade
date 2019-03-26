package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import rx.subjects.PublishSubject;


@Service
public class ChannelServiceRxJava {
    Log log = LogFactory.getLog(getClass());
    PublishSubject<Foo> subject = PublishSubject.create();

    private final MessageChannel broadcast, direct;

    ChannelServiceRxJava(ProducerChannels channels){
        this.broadcast = channels.broadcastGreetings();
        this.direct = channels.directGreetings();
    }

    public void publish(Foo payload){
        this.direct.send(MessageBuilder.withPayload(payload).build());
        this.broadcast.send(MessageBuilder.withPayload(payload).build());
    }


}
