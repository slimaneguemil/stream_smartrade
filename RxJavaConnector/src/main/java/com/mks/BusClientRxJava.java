package com.mks;

import org.springframework.beans.factory.annotation.Autowired;
import rx.Observer;
import rx.Subscription;

public class BusClientRxJava implements  BusClientInterface{

    @Autowired
    ChannelServiceRxJava channelServiceRxJava;


    @Override
    public void publish(Foo payload) {
        channelServiceRxJava.publish(payload);
    }

    @Override
    public Subscription subscribe(Observer<Foo> t) {
        return channelServiceRxJava.subscribe(t);
    }

    @Override
    public void unSubscribe(Subscription s) {
        channelServiceRxJava.unSubscribe(s);
    }
}

