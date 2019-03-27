package com.mks;

import rx.Observer;
import rx.Subscription;

public interface InterfaceRxJava {
    public void publish(Foo payload);
    public Subscription subscribe(Observer<Foo> t);
    public void unSubscribe(Subscription s);
}
