package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;


@RestController
@SpringBootApplication
public class RunSimulatorRxJava implements CommandLineRunner {

    @Autowired
    ChannelServiceRxJava channelServiceRxJava;

    Log log = LogFactory.getLog(getClass());
    //ChannelServiceRxJava channelService;

    public static void main(String args[]) {
        SpringApplication.run(RunSimulatorRxJava.class, args);

    }

    public void run(String[] args) {
        //PublishSubject<Foo> subject = channelServiceRxJava.getSubject();

       // Subscription s1 = subject.subscribe(getFirstObserver());
//        Subscription s1 = channelServiceRxJava.subscribe(getFirstObserver());
//        Subscription s2 = channelServiceRxJava.subscribe(getSecondObserver());
//
//        Foo foo1 = new Foo();
//        foo1.setId(100);
//        foo1.setName("rxjva message 1");
//        foo1.setTag("1");
//        channelServiceRxJava.publish(foo1);
//
//        Foo foo2 = new Foo();
//        foo2.setId(100);
//        foo2.setName("rxjava message 2");
//        foo2.setTag("1");
//        channelServiceRxJava.publish(foo2);
//       channelServiceRxJava.unSubscribe(s1);
//        //s1.unsubscribe();
//
//        Foo foo3 = new Foo();
//        foo3.setId(100);
//        foo3.setName("rx java message 3");
//        foo3.setTag("1");
//        channelServiceRxJava.publish(foo3);


    }

    Observer<Foo> getFirstObserver() {
        return new Observer<Foo>() {

            @Override
            public void onNext(Foo message) {
                System.out.println("RxJava Subscriber1: " + message);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("RxJava  error");
            }

            @Override
            public void onCompleted() {
                System.out.println("RxJava  Subscriber1 completed");
            }
        };
    }
    Observer<Foo> getSecondObserver() {
        return new Observer<Foo>() {

            @Override
            public void onNext(Foo message) {
                System.out.println("RxJava Subscriber2: " + message);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("RxJava Subscriber2 completed");
            }
        };
    }

    @RequestMapping("/publish/{name}")
    String hi(@PathVariable String name) {
        String message = "Hello, " + name + "!";
        //System.out.println("received from object rest :"+ message);
        Foo foo1 = new Foo();
        foo1.setId(100);
        foo1.setName(message);
        foo1.setTag("1");

        channelServiceRxJava.publish(foo1);
        return "//nSuccess";
    }


}