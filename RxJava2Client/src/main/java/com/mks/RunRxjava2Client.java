/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mks;

import com.mks.broker.BusClientInterface;
import com.mks.broker.BusClientRxJava2;
import com.mks.broker.utils.Deal;
import com.mks.broker.utils.utils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Sample app demonstrating a polled consumer where the application can
 * control the rate at which Deals are retrieved.
 *
 * @author Gary Russell
 */
@SpringBootApplication
public class RunRxjava2Client {
   private static final long start = System.currentTimeMillis();

    public static final ExecutorService exec = Executors.newSingleThreadExecutor();
    public static void main(String[] args) {
        ApplicationContext context=
            SpringApplication.run(RunRxjava2Client.class, args);
        BusClientInterface bus = context.getBean(BusClientRxJava2.class);


        bus.subscribe(BusClientInterface.Bus.DEALS, getSubscriber());

    }




    public static Subscriber<Deal> getSubscriber() {
        return new Subscriber<Deal>() {
            AtomicLong count = new AtomicLong(0);
            Subscription s;


            public void onSubscribe(Subscription subscription) {
                this.s = subscription;
                subscription.request(1);
            }


            public void onNext(Deal Deal) {
                utils.log("@NewSubscriber 1 received = "+Deal) ;
                Deal.setEnd(System.currentTimeMillis());
                long timing = Deal.getEnd() - Deal.getStart();
                utils.log(Deal.getEnd() + "-" + Deal.getStart() + "->" + timing);
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(700 ));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                // if (count.incrementAndGet() == 2)
//                //    s.cancel();
                s.request(1);
            }

            public void onError(Throwable throwable) {
                System.out.println("ERRRRRORRRRRRRRRRRRRRRRRRRRRRRRR throwable = " + throwable);
            }

            public void onComplete() {
                System.out.println("on complete = " + count);
            }
        };
    }

    @Bean("myBus")
    BusClientInterface busClientRxJava() {
        return new BusClientRxJava2();
    }
}
