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

import com.mks.utils.Deal;
import com.mks.utils.Deal2;
import com.mks.utils.utils;
import io.reactivex.Observable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Sample app demonstrating a polled consumer where the application can
 * control the rate at which Deals are retrieved.
 *
 * @author Gary Russell
 */
@SpringBootApplication

public class RunPollDemo {
   private static final long start = System.currentTimeMillis();

    public static final ExecutorService exec = Executors.newSingleThreadExecutor();
    public static void main(String[] args) {
            SpringApplication.run(RunPollDemo.class, args);
    }


    @Bean
    public ApplicationRunner runner( MessageChannel output, FlowService bus) {
        return args -> {
            System.out.println("@Bean : starting");
//            bus.getFlow()
//                    .subscribe(getSubscriber());

            exec.execute(() -> {
                boolean result = false;
                //Observable.interval(2000, TimeUnit.MILLISECONDS)
                Observable.range(1, 30)
                        .takeUntil(i -> i > 20000)

//                        .map(i -> {
//                            long l = System.currentTimeMillis();
//                            utils.log("from @Bean: new Deal :" + i + "time:" + l);
//                            return utils.getDeal(i.intValue(), "from @Bean", l);
//                        })
                        .publish().autoConnect()

                        .subscribe(s -> {
                            long l = System.currentTimeMillis();
                            utils.log("from @Bean: new Deal :" + s + "time:" + l);
                            if (s==2){
                                Deal2 m2 = new Deal2();
                                m2.setName("toto");
                                m2.setStart(System.currentTimeMillis() );
                                output.send((MessageBuilder.withPayload(m2).build()));
                            }
                               else
                            output.send(MessageBuilder.withPayload(utils.getDeal(s.longValue(), "from @Bean", l))
                                    .build());
                            //utils.sleep(utils.sleepRandom(500));
                                utils.sleep(50);
                        });

            });
        };
    }


    public static Subscriber<Deal> getSubscriber() {
        return new Subscriber<Deal>() {
            AtomicLong count = new AtomicLong(0);
            Subscription s;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.s = subscription;
                subscription.request(1);
            }

            @Override
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

    public static void generateDeals(FlowService bus) {

        System.out.println("@generateDeals staring ...");
        Observable.interval(2, TimeUnit.SECONDS)
                // Observable.range(1, 1)
                // .map( v -> utils.intenseCalculation(v))
                .map(v -> {
                    return utils.getDeal(v, "from @generatefoos", System.currentTimeMillis());
                })
                .subscribe(s -> {
                            System.out.println("@generateDeals : sending from generateDeals = " + s);
                            bus.send(MessageBuilder.withPayload(s)
                                    .setHeader("start", System.currentTimeMillis())
                                    .build());
                        }
                );


    }


}
