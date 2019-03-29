package com.mks.processor;

import com.mks.BusClientInterface;
import com.mks.BusClientRxJava;
import com.mks.Foo;
import com.mks.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observer;
import rx.Subscription;

import static org.junit.Assert.assertTrue;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
@RunWith(SpringRunner.class)
public class RunProcessorTest {

    @Autowired
    ApplicationContext context;


    @Test
    public void whenAllBeansCreated_FileTextEndsWithProcessed() {
         //BusClientInterface bus = (BusClientRxJava) context.getBean("testBus");
        BusClientInterface bus = (BusClientRxJava) context.getBean("testBus");

        Subscription s1 = bus.subscribe(BusClientInterface.Bus.DEALS, getFirstObserver());

            Foo foo1 = new Foo();
            foo1.setId(100);
        foo1.setName("rxjva message 1");
        foo1.setTag("1");
        bus.publish(BusClientInterface.Bus.DEALS, foo1);

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
}
