package com.mks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

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
        BusClientInterface bus = (BusClientRxJava) context.getBean("myBus");
        Subscription s1 = bus.subscribe(BusClientInterface.Bus.DEALS, RunSimulatorRxJava.getFirstObserver());
        Foo foo1 = new Foo();
        foo1.setId(100);
        foo1.setName("rxjva message 1");
        foo1.setAmount(1);
        bus.publish(BusClientInterface.Bus.DEALS, foo1);

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

    @Bean("myBus")
    BusClientInterface busClientRxJava() {
        return new BusClientRxJava();
    }

}