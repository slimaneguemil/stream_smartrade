package com.mks;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Subscription;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class ChannelServiceRxJava implements BusClientInterface {
    Log log = LogFactory.getLog(getClass());
    Subject<Foo> subject_channel1 = PublishSubject.create();
    Subject<Foo> subject_channel2 = PublishSubject.create();

    private final MessageChannel channel1, channel2;



    ChannelServiceRxJava(ProducerChannels channels){
        this.channel1 = channels.output1();
        this.channel2 = channels.output2();
    }

    public Subject<Foo> getSubject_channel1() {
        return subject_channel1;
    }
    public Subject<Foo> getSubject_channel2() {
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

    public Disposable subscribe(Bus bus, Observer<Foo> t){

        switch (bus){
            case DEALS:

                 this.subject_channel1.subscribe(t);

            case SYSTEM_EVENTS:
                this.subject_channel2.subscribe(t);

            default:
                return null;

        }

    }

    public void unSubscribe(Disposable d){
        d.dispose();
    }

  

}
