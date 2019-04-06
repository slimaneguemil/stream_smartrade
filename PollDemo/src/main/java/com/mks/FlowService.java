package com.mks;

import com.mks.utils.Message;
import com.mks.utils.utils;
import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component

public class FlowService {
    public static final ExecutorService exec = Executors.newSingleThreadExecutor();

    PollableMessageSource pollChannel;
    MessageChannel output;
    Flowable<Message> flow;


    public void subscribe(Subscriber<Message> s) {

        this.flow.subscribe(s);
    }

    public void test() {
        //        flow = Flowable.interval(1, TimeUnit.SECONDS)
//                .map(s -> generate(pollChannel))


        flow = rangeReverse2(0,1000)
               // .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .publish()
                .autoConnect();
/*
        flow= rangeReverse(200, 0)
                .subscribeOn(Schedulers.computation())
                .doOnNext(i -> System.out.println("Emitting " +
                        i))
                .observeOn(Schedulers.io());*/

    }

    FlowService(PollableMessageSource pollChannel, MessageChannel output) {
        this.pollChannel = pollChannel;
        this.output = output;


        flow = rangeReverse2(0,200)
               // .subscribeOn(Schedulers.computation())
                .onBackpressureBuffer(2000,
                        ()-> utils.log("Buffer Overflow ********************** " )
                , BackpressureOverflowStrategy.ERROR)
                .observeOn(Schedulers.io())
                .publish()
                .refCount(1);


    }

   Flowable<Message> rangeReverse(int upperBound, int
            lowerBound) {
        return Flowable.generate(() -> new
                        AtomicInteger(upperBound + 1),
                (state, emitter) -> {
                    int current = state.decrementAndGet();
                    Message payload = utils.getMessage(current, "mock", 0);
                    utils.log("pollling frombroker: " + payload);
                    emitter.onNext(payload);

                    if (current == lowerBound)
                        emitter.onComplete();
                }
        );
    }

    Flowable<Message> rangeReverse2( int lowerBound, int upperBound) {
        return Flowable.generate(() -> new
                        AtomicInteger(lowerBound + 1),
                (state, emitter) -> {

                    int current = state.incrementAndGet();
                    boolean result = false;
                    //utils.log("pollling renverse2 " + current);

                    result = this.pollChannel.poll(m -> {
                        Message payload = (Message) m.getPayload();
                        utils.log("pollling frombroker: " + payload);
                        emitter.onNext(payload);

                    }, new ParameterizedTypeReference<Message>() {
                    });
                    utils.sleep(50);
//                    if (current == lowerBound)
//                        emitter.onComplete();
                }
        );
    }

    AtomicInteger count = new AtomicInteger(1);

    Flowable<Message> mockMessage(PollableMessageSource input) {
        return Flowable.generate(emitter -> {
                    boolean result = false;

                        Message payload = utils.getMessage(count.incrementAndGet(),"mock",0);
                        utils.log("pollling frombroker: " + payload);
                        emitter.onNext(payload);
                       utils.sleep(50);

//                    result = input.poll(m -> {
//                        Message payload = (Message) m.getPayload();
//                        utils.log("pollling frombroker: " + payload);
//                        emitter.onNext(payload);
//
//                    }, new ParameterizedTypeReference<Message>() {
//                    });
//                    utils.sleep(1000);

                }
        );
    }

    Flowable<Message> generateMessage(PollableMessageSource input) {
        return Flowable.generate(emitter -> {
                    boolean result = false;
//                    utils.log("@Flowable Service : starintg listening");

                    result = input.poll(m -> {
                        Message payload = (Message) m.getPayload();
                        utils.log("pollling frombroker: " + payload);
                        emitter.onNext(payload);
                        utils.sleep(2000);
                    }, new ParameterizedTypeReference<Message>() {
                    });


                    if (result) {
//                        utils.log("@Flowable Service : receiving message");
                        //  System.out.print("flowservice : emit Message successfully");
                    }

                }
        );
    }

    Flowable<Message> generateMessage2(PollableMessageSource input) {
        return Flowable.generate(emitter -> {
                    boolean result = false;
                    utils.log("@Flowable Service : starintg listening");

                    result = input.poll(m -> {
                        Message payload = (Message) m.getPayload();
                        utils.log("pollling frombroker: " + payload);
                        emitter.onNext(payload);

                    }, new ParameterizedTypeReference<Message>() {
                    });


                    if (result) {
                        utils.log("@Flowable Service : receiving message");
                        //  System.out.print("flowservice : emit Message successfully");
                    }

                }
        );
    }

    Flowable<Message> generateMessageExec(PollableMessageSource input) {
        return Flowable.generate(emitter -> {

                    exec.execute(() -> {
                        boolean result = false;

                        try {
                            result = input.poll(m -> {
                                Message payload = (Message) m.getPayload();
                                //  utils.log("pollling frombroker: " + payload);
                                emitter.onNext(payload);

                            }, new ParameterizedTypeReference<Message>() {
                            });

                            Thread.sleep(1_000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();

                        }
                        if (result) {
                            utils.log("@Flowable Service : receiving message");
                            //  System.out.print("flowservice : emit Message successfully");
                        }
                    });
                }
        );
    }

    Message generate(PollableMessageSource input) {
        boolean result = false;
        Message payload;

        boolean poll = input.poll(FlowService::handleMessage, new ParameterizedTypeReference<Message>() {
        });

        return null;

    }

    Observable<Message> generateObs(PollableMessageSource input) {
        return Observable.generate(emitter -> {
                    boolean result = false;

                    try {
                        result = input.poll(m -> {
                            Message payload = (Message) m.getPayload();
                            System.out.println("pollling frombroker: " + payload);
                            //System.out.println("******************************33 m = " + m.getPayload() + ""+ m.getClass());
                            emitter.onNext(payload);

                        }, new ParameterizedTypeReference<Message>() {
                        });
                    } catch (Exception e) {
                        Thread.currentThread().interrupt();

                    }
                    if (result) {
                        System.out.print("flowservice : emit Message successfully");

                    }
                    System.out.println(".");
                    utils.sleep(1000);
                }
        );
    }

    private static void handleMessage(org.springframework.messaging.Message<?> m) {
        Message payload = (Message) m.getPayload();
    }

    public MessageChannel getOutput() {
        return output;
    }


    public Flowable<Message> getFlow() {
        return flow;
    }

//    @StreamListener("errorChannel")
//    public void error ( Message<Message> message){
//        System.out.println("error channel message = " + message);
//    }
}
