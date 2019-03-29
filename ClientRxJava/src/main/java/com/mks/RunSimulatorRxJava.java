package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observer;
import rx.Subscription;


@RestController
@SpringBootApplication
public class RunSimulatorRxJava implements CommandLineRunner {

    Log log = LogFactory.getLog(getClass());
    //@Autowired
    //private ApplicationContext context;

    public static void main(String args[]) {
        ApplicationContext context = SpringApplication.run(RunSimulatorRxJava.class, args);
        //BusClientInterface bus = context.getBean(BusClientRxJava.class);
        BusClientInterface bus = (BusClientRxJava)context.getBean("myBus");
        //  BusClientInterface bus = new BusClientRxJava();
        Subscription s1 = bus.subscribe(BusClientInterface.Bus.DEALS, RunSimulatorRxJava.getFirstObserver());
        //    Foo foo1 = new Foo();
        //      foo1.setId(100);
//        foo1.setName("rxjva message 1");
//        foo1.setTag("1");
//        busClientRxJava().publish(BusClientInterface.Bus.DEALS, foo1);

    }

    public void run(String[] args) {

    }

    static Observer<Foo> getFirstObserver() {
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

        busClientRxJava().publish(BusClientInterface.Bus.DEALS, foo1);
        return "//nSuccess";
    }


    @Bean("myBus")
    BusClientInterface busClientRxJava(){
        return new BusClientRxJava();
    }

}