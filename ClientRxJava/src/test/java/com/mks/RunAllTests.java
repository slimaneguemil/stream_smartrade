package com.mks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunAllTests {

    @Autowired
    ChannelServiceRxJava channelServiceRxJava;

    Integer subscriber1 = 0;
    Integer subscriber2 = 0;

    Observer<Foo> getObserver1() {
        return new Observer<Foo>() {

            @Override
            public void onNext(Foo value) {
                subscriber1 += value.getId();
                System.out.println("Subscriber1: " + value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("Subscriber1 completed");
            }
        };
    }
    Observer<Foo> getObserver2() {
        return new Observer<Foo>() {

            @Override
            public void onNext(Foo value) {
                subscriber2 += value.getId();
                System.out.println("Subscriber2: " + value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("Subscriber2completed");
            }
        };
    }

    @Test
    public void test1(){
        try {
        List<Foo> elements = new ArrayList<>();

            Subscription s1 = channelServiceRxJava.subscribe(getObserver1());
            Subscription s2 = channelServiceRxJava.subscribe(getObserver2());
        Foo foo1 = new Foo();
        foo1.setId(1);
        foo1.setName("rxjva message 1");
        foo1.setTag("1");
        channelServiceRxJava.publish(foo1);
        channelServiceRxJava.publish(foo1);
        channelServiceRxJava.publish(foo1);
       // s1.unsubscribe();

       Thread.sleep(1000);
       s1.unsubscribe();
       channelServiceRxJava.publish(foo1);

        System.out.println("subscriber1 = " + subscriber1);
        assertTrue(subscriber1 + subscriber2 == 7);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){

//        List<Foo> elements = new ArrayList<>();
//
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
//        channelServiceRxJava.unSubscribe(s1);
//        //s1.unsubscribe();
//
//        Foo foo3 = new Foo();
//        foo3.setId(100);
//        foo3.setName("rx java message 3");
//        foo3.setTag("1");
//        channelServiceRxJava.publish(foo3);

    }
    @Test
    public void test3() {
        PublishSubject<Foo> subject = channelServiceRxJava.getSubject();

        subject.subscribe(getObserver1());
        Foo foo1 = new Foo();
        foo1.setId(1);
        foo1.setName("rxjva message 1");
        foo1.setTag("1");
        subject.onNext(foo1);
        subject.onNext(foo1);
        subject.onNext(foo1);


        assertTrue(subscriber1 + subscriber2 == 3);
    }


    Observer<Foo> getFirstObserver() {
        return new Observer<Foo>() {

            @Override
            public void onNext(Foo message) {
                subscriber1 += message.getId();
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

}
