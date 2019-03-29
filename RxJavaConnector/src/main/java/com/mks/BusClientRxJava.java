package com.mks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import rx.Observer;
import rx.Subscription;

//@Service
//@Configurable
public class BusClientRxJava implements  BusClientInterface{

    @Autowired
    ChannelServiceRxJava channelServiceRxJava;


    @Override
    public void publish(BusClientInterface.Bus bus ,Foo payload) {
        channelServiceRxJava.publish(bus, payload);
    }

    @Override
    public Subscription subscribe(BusClientInterface.Bus bus , Observer<Foo> t) {
        return channelServiceRxJava.subscribe(bus, t);
    }

    @Override
    public void unSubscribe(Subscription s) {
        channelServiceRxJava.unSubscribe(s);
    }
}

