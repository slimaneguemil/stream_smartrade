package com.mks;

import rx.Observer;
import rx.Subscription;

public interface BusClientInterface {
    public void publish(BusClientInterface.Bus bus ,Foo payload);
    public Subscription subscribe(BusClientInterface.Bus bus ,Observer<Foo> t);
    public void unSubscribe(Subscription s);

    public enum Bus{
        DEALS,
        SYSTEM_EVENTS
    }
}
