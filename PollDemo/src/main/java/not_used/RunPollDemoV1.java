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
import com.mks.utils.utils;
import io.reactivex.Observable;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Sample app demonstrating a polled consumer where the application can
 * control the rate at which messages are retrieved.
 *
 * @author Gary Russell
 */
//@SpringBootApplication

public class RunPollDemoV1 {

    public static final ExecutorService exec = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RunPollDemoV1.class, args);
        FlowService bus = context.getBean(FlowService.class);
        bus.subscribe(getSubscriber());
        bus.getFlow().subscribe(s -> System.out.println("@flowable subscriber = " + s));
        //generateMessages(bus);
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
            public void onNext(Deal Message) {
                System.out.println("@NewSubscriber 1 received = " + Message);
                if (count.incrementAndGet() == 2)
                    s.cancel();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public static void generateMessages(FlowService bus) {

        System.out.println("@generateMessages staring ...");
        Observable.interval(2, TimeUnit.SECONDS)
                // Observable.range(1, 1)
                // .map( v -> utils.intenseCalculation(v))
                .map(v -> {
                    return utils.getDeal(v, "from @generatefoos",System.currentTimeMillis());
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
    public ApplicationRunner runner(MessageChannel output) {
        return args -> {
            System.out.println("@Bean : starting");
            exec.execute(() -> {
                boolean result = false;
                Observable.interval(2, TimeUnit.SECONDS)
                        //Observable.range(1, 1)
                        .map(i -> {
                            return utils.getDeal(i, "from @Bean",System.currentTimeMillis());
                        }).subscribe(s -> {
                    output.send(MessageBuilder.withPayload(s)
                            .build());

                });
            });
        };
    }


}
