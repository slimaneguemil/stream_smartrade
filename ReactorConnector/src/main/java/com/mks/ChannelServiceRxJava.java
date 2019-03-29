package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;


@Service
public class ChannelServiceRxJava<T> implements BusClientInterface {
    Log log = LogFactory.getLog(getClass());
     PublishSubject<Foo> subject = PublishSubject.create();
    private final MessageChannel broadcast;

    ChannelServiceRxJava(ProducerChannels channels){
        this.broadcast = channels.broadcastGreetings();
        //this.direct = channels.directGreetings();
    }

    public PublishSubject<Foo> getSubject() {
        return subject;
    }

    public void publish(Foo payload){
        //this.direct.send(MessageBuilder.withPayload(payload).build());
        this.broadcast.send(MessageBuilder.withPayload(payload).build());
    }

    public Subscription subscribe(Observer<Foo> t){
           return  this.subject.subscribe(t);
    }
    public void unSubscribe(Subscription s){
        s.unsubscribe();
    }


}
