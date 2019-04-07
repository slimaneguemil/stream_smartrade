package com.mks;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.reactivestreams.Subscription;

public interface BusClientInterface {
    public void publish(BusClientInterface.Bus bus ,Foo payload);
    public Disposable subscribe(BusClientInterface.Bus bus , Observer<Foo> t);
    public void unSubscribe(Disposable d);

    public enum Bus{
        DEALS,
        SYSTEM_EVENTS
    }
}
