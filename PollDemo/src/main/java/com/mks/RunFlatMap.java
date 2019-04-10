package com.mks;

import com.mks.utils.Deal;
import com.mks.utils.utils;
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
    static int service_buffer  ;

    public static void main(String[] args) {
        //ApplicationContext context =
                SpringApplication.run(RunFlatMap.class, args);
        utils.log("Launching program");
        //FlowService bus = context.getBean(FlowService.class);
        System.out.println("service = " + service_buffer);
        //bus
          //      .subscribe(getSubscriber());

//        bus.getFlow()
//                .window(5)
//.flatMapSingle(obs -> obs.reduce("", (s,v)-> v))
//                .subscribe( sa -> utils.log("*******************************  window = " + sa));

//
//        bus.getFlow()
//                .buffer(20,TimeUnit.MILLISECONDS, 1)
//                .doOnNext(s -> System.out.println("s = " + s))
//                .subscribe(RunFlatMap.getSubscriber());


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
                 utils.log("subsriber get m = " + message );
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
   /* public static Subscriber2<List<Deal>> getSubscriber() {
        return new Subscriber<List<Deal>>() {
            AtomicLong count = new AtomicLong(0);
            Subscription s;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.s = subscription;
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(List<Deal> messages) {
                for (Deal m : messages){
                    utils.log("subsriber get m = " + m);
                    //utils.sleep(500);
                }
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
*/

}


