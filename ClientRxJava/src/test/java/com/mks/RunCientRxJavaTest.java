package com.mks;

import com.mks.utils.MyFunctions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observer;
import rx.Subscription;


import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "/application-test.properties")
//@TestPropertySource(properties = "mks.topic=test5")

@SpringBootTest
public class RunCientRxJavaTest {

//    @Autowired
//    ChannelServiceRxJava channelServiceRxJava;

    @Autowired
    private ApplicationContext context;

    @Bean("testBus")
    BusClientInterface busClientRxJava(){
        return new BusClientRxJava();
    }



    @Test
    public void test_publish_subscribe_consistency(){
        try {
            BusClientInterface bus = (BusClientRxJava)context.getBean("testBus");
            Subscription s1 = bus.subscribe(BusClientInterface.Bus.DEALS, MyFunctions.getObserver());

            Foo foo1 = new Foo();
            foo1.setId(1);
            foo1.setName("rxjva message 1");
            foo1.setTag("1");
            bus.publish(BusClientInterface.Bus.DEALS,foo1);

            Thread.sleep(2000);
            assertThat(MyFunctions.fooList.size()).isEqualTo(1);
            assertThat(MyFunctions.fooList.get(0)).isEqualToComparingFieldByField(foo1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void test_publish_subscribe_unsubscribe_consistency(){
        try {
            BusClientInterface bus = (BusClientRxJava)context.getBean("testBus");

            //step1 : we subscribe 2 subscribers 1 and 2
            Subscription s1 = bus.subscribe(BusClientInterface.Bus.DEALS, MyFunctions.getObserver1());
            Subscription s2 = bus.subscribe(BusClientInterface.Bus.DEALS, MyFunctions.getObserver2());

            //step2 : we publish
            Foo foo1 = new Foo();
            foo1.setId(1);
            foo1.setName("rxjva message 1");
            foo1.setTag("1");
            bus.publish(BusClientInterface.Bus.DEALS,foo1);
            bus.publish(BusClientInterface.Bus.DEALS,foo1);
            bus.publish(BusClientInterface.Bus.DEALS,foo1);


            //step 3: we unsubscribe subscriber1
            //
            Thread.sleep(1000);
            s1.unsubscribe();
            bus.publish(BusClientInterface.Bus.DEALS,foo1);
            Thread.sleep(1000);
            // we sum foo.id received by subscriber 1 and 2
            // subscriber1 received 3 times -> sum = 3
            // sunscriber2 received 4 times -> sum = 4
            // subscriber 1 and 2 recived -> sum = 3+4
            assertTrue(MyFunctions.subscriber1 + MyFunctions.subscriber2 == 7);



        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
