package com.mks.back;

import com.mks.utils.Message;
import com.mks.utils.utils;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component

public class FlowService2 {
    public static final ExecutorService exec = Executors.newSingleThreadExecutor();

    PollableMessageSource pollChannel;
    MessageChannel output;
    Flowable<Message> flow;



    public void subscribe(Subscriber<Message> s) {

        this.flow.subscribe(s);
    }

    FlowService2(PollableMessageSource pollChannel, MessageChannel output) {
        this.pollChannel = pollChannel;
        this.output = output;

//        flow = Flowable.interval(1, TimeUnit.SECONDS)
//                .map(s -> generate(pollChannel))

                flow= mockMessage(pollChannel)
                        .onBackpressureBuffer(400)
                      //  .subscribeOn(Schedulers.newThread())//.delay(1,TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .doOnError(s -> System.out.println("doOnerror = " + s))
                        .doOnTerminate(() -> System.out.println("do ontermnate = " ))
                .doOnSubscribe(s -> System.out.println("doOnsubscribe = " + s))
                .doOnComplete(() -> System.out.println("doOnclomplete = "))
                .observeOn(Schedulers.io())
              //          .observeOn(Schedulers.computation(),1024)
                .publish()
                .refCount(1);
    }

    AtomicInteger count = new AtomicInteger(1);
    Flowable<Message> mockMessage(PollableMessageSource input) {
        return Flowable.generate(emitter -> {
                    boolean result = false;

                        Message payload = utils.getMessage(count.incrementAndGet(),"mock",0);
                        utils.log("pollling frombroker: " + payload);
                        emitter.onNext(payload);
                        utils.sleep(500);

                    if (result) {
//                        utils.log("@Flowable Service : receiving message");
                        //  System.out.print("flowservice : emit Message successfully");
                    }

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

        boolean poll = input.poll(FlowService2::handleMessage, new ParameterizedTypeReference<Message>() {
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
