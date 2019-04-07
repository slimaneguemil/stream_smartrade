package com.mks;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

//@Service
//@Configurable
public class BusClientRxJava implements  BusClientInterface{

    @Autowired
    ChannelServiceRxJava channelServiceRxJava;
    public Subject<Foo> getSubject_channel1() {
        return channelServiceRxJava.getSubject_channel1();
    }

    @Override
    public void publish(BusClientInterface.Bus bus ,Foo payload) {
        channelServiceRxJava.publish(bus, payload);
    }

    @Override
    public Disposable subscribe(BusClientInterface.Bus bus , Observer<Foo> t) {
        return channelServiceRxJava.subscribe(bus, t);
    }

    @Override
    public void unSubscribe(Disposable d) {
        channelServiceRxJava.unSubscribe(d);
    }
}

