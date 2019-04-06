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

import com.mks.utils.Message;
import com.mks.utils.utils;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Sample app demonstrating a polled consumer where the application can
 * control the rate at which messages are retrieved.
 *
 * @author Gary Russell
 */
@SpringBootApplication

public class RunPollDemo {
   private static final long start = System.currentTimeMillis();

    public static final ExecutorService exec = Executors.newSingleThreadExecutor();
    static CountDownLatch latch ;
    public static void main(String[] args) {
        latch = new CountDownLatch(1);
        try {
        ApplicationContext context = SpringApplication.run(RunPollDemo.class, args);
        utils.log("Launching program");
        FlowService bus = context.getBean(FlowService.class);
        bus.subscribe(getSubscriber());
        //bus.getFlow().subscribe(s -> System.out.println("@flowable subscriber = " + s));
        //generateMessages(bus);
//            bus.rangeReverse2(0,200)
//                    .onBackpressureDrop()
//                    .observeOn(Schedulers.io())
//                    .publish()
//                    .autoConnect()
//                    .doOnNext(s -> System.out.println("do On next = " + s))
//                    .subscribe(i -> {
//                        utils.sleep(200);
//                        System.out.println("Received " + i);
//                    });

            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static Subscriber<Message> getSubscriber() {
        return new Subscriber<Message>() {
            AtomicLong count = new AtomicLong(0);
            Subscription s;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.s = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(Message Message) {
                utils.log("@NewSubscriber 1 received = "+Message) ;
                Message.setEnd(System.currentTimeMillis());
                long timing = Message.getEnd() - Message.getStart();
                utils.log(Message.getEnd() + "-" + Message.getStart() + "->" + timing);
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(700 ));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                // if (count.incrementAndGet() == 2)
//                //    s.cancel();
                s.request(1);
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

    public static void generateMessages(FlowService bus) {

        System.out.println("@generateMessages staring ...");
        Observable.interval(2, TimeUnit.SECONDS)
                // Observable.range(1, 1)
                // .map( v -> utils.intenseCalculation(v))
                .map(v -> {
                    return utils.getMessage(v.intValue(), "from @generatefoos", System.currentTimeMillis());
                })
                .subscribe(s -> {
                            System.out.println("@generateMessages : sending from generateMessages = " + s);
                            bus.getOutput().send(MessageBuilder.withPayload(s)
                                    .setHeader("start", System.currentTimeMillis())
                                    .build());
                        }
                );


    }


    @Bean
    public ApplicationRunner runner(PollableMessageSource input, MessageChannel output) {
        return args -> {
            System.out.println("@Bean : starting");
            exec.execute(() -> {
                boolean result = false;
                //Observable.interval(2000, TimeUnit.MILLISECONDS)
                Observable.range(1, 30000)
                        .takeUntil(i -> i > 2000)

//                        .map(i -> {
//                            long l = System.currentTimeMillis();
//                            utils.log("from @Bean: new Message :" + i + "time:" + l);
//                            return utils.getMessage(i.intValue(), "from @Bean", l);
//                        })
                        .publish().autoConnect()
                        .delay(2, TimeUnit.SECONDS)
                        .subscribe(s -> {
                            long l = System.currentTimeMillis();
                            utils.log("from @Bean: new Message :" + s + "time:" + l);
                            output.send(MessageBuilder.withPayload(utils.getMessage(s.intValue(), "from @Bean", l))
                                    .build());
                            //utils.sleep(utils.sleepRandom(500));
                                utils.sleep(1000);
                        });

            });
        };
    }


}
