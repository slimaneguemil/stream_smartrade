package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;

@Service
public class ChannelServiceRxJava implements BusClientInterface {
    Log log = LogFactory.getLog(getClass());
    PublishSubject<Foo> subject_channel1 = PublishSubject.create();
    PublishSubject<Foo> subject_channel2 = PublishSubject.create();

    private final MessageChannel channel1, channel2;



    ChannelServiceRxJava(ProducerChannels channels){
        this.channel1 = channels.output1();
        this.channel2 = channels.output2();
    }

    public PublishSubject<Foo> getSubject_channel1() {
        return subject_channel1;
    }
    public PublishSubject<Foo> getSubject_channel2() {
        return subject_channel2;
    }

    public void publish(BusClientInterface.Bus bus, Foo payload){
        //this.direct.send(MessageBuilder.withPayload(payload).build());
        switch (bus){
            case DEALS:
                this.channel1.send(MessageBuilder.withPayload(payload).build());
                break;
            case SYSTEM_EVENTS:
                this.channel2.send(MessageBuilder.withPayload(payload).build());
                break;
        }

    }

    public Subscription subscribe(BusClientInterface.Bus bus, Observer<Foo> t){

        switch (bus){
            case DEALS:
                return  this.subject_channel1.subscribe(t);


            case SYSTEM_EVENTS:
                return  this.subject_channel2.subscribe(t);

            default:
                return null;

        }

    }

    public void unSubscribe(Subscription s){
        s.unsubscribe();
    }


}
