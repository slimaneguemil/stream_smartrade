package com.mks;

import com.mks.utils.Deal;
import com.mks.utils.utils;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.export.appoptics.AppOpticsMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.atomic.AtomicLong;


@SpringBootApplication
@PropertySource("classpath:test.properties")
public class RunFlatMap {

    @Value("${test}")
    static int service_buffer;

    public static void main(String[] args) {
        ApplicationContext context =
                SpringApplication.run(RunFlatMap.class, args);
        utils.log("Launching program");
        FlowService bus = context.getBean(FlowService.class);
        System.out.println("service = " + service_buffer);
        //bus
        //      .subscribe(getSubscriber());

        bus.getFlow().flatMap(
                s ->
                        RunFlatMap.asyncLoadBy(s)
                .subscribeOn(Schedulers.computation())
        );


    }

    public static Flowable<Deal> asyncLoadBy(Deal d) {
        return Flowable.fromCallable(() -> slowLoadBy(d));
    }

    public static Deal slowLoadBy(Deal d) {
        utils.sleep(1000);
        utils.log(d);
        return d;
    }

    public static Subscriber<Deal> getSubscriber() {
        return new Subscriber<Deal>() {
            AtomicLong count = new AtomicLong(0);
            Subscription s;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.s = subscription;
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Deal message) {
                utils.log("subsriber get m = " + message);
                utils.sleepRandom(100);

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("ERRRRRORRRRRRRRRRRRRRRRRRRRRRRRR throwable = " + throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("on complete = " + count);
            }
        };
    }


}


