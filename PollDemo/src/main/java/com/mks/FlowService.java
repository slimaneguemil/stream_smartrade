package com.mks;

import com.mks.utils.Deal;
import com.mks.utils.utils;
import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Component

public class FlowService {
    @Value("${mks.service.buffer:2000}")
    int service_buffer;

    public static final ExecutorService exec = Executors.newSingleThreadExecutor();
    Channels.PolledProcessor polledProcessor;
    PollableMessageSource channelPollDeals, channelPollLogs;
    MessageChannel channelOutputDeals, channelOutputLogs;
    Flowable<Deal> flow;
    AtomicLong count = new AtomicLong(1);


    public void subscribe(Subscriber<Deal> s) {

        this.flow
               // .buffer(1, TimeUnit.SECONDS)
                .subscribe(s);
    }

    FlowService(Channels.PolledProcessor polledProcessor) {
        this.polledProcessor = polledProcessor;
        this.channelPollDeals = polledProcessor.input();
        this.channelPollLogs = polledProcessor.input2();
        this.channelOutputDeals = polledProcessor.output();
        this.channelOutputLogs = polledProcessor.output2();

        flow = generateFlow(0,200)
                .subscribeOn(Schedulers.computation())
                .onBackpressureBuffer(1000,
                        ()-> utils.log("Buffer Overflow ********************** " )
                , BackpressureOverflowStrategy.ERROR)
                .observeOn(Schedulers.io())
                .publish()
                .refCount(1);
    }

    public void send(Message m){
       this.getOutput().send(m);
    }
    Flowable<Deal> generateFlow( int lowerBound, int upperBound) {
        return Flowable.generate(() -> new
                        AtomicLong(lowerBound + 1),
                (state, emitter) -> {

                    Long current = state.incrementAndGet();
                    boolean result = false;
                    //utils.log("pollling renverse2 " + current);

                    result = this.channelPollDeals.poll(m -> {

                        Deal payload = (Deal) m.getPayload();
                        utils.log("pollling frombroker: "+ payload) ;
                        emitter.onNext(payload);
                        this.channelOutputLogs.send(MessageBuilder.withPayload(payload).build());

                    }, new ParameterizedTypeReference<Deal>() {
                    });
                    utils.sleep(10);
                    if(result)
                        this.channelOutputLogs.send(MessageBuilder.withPayload("flowservice: SUCCESS").build());
//                    if (current == lowerBound)
//                        emitter.onComplete();
                }
        );
    }


    public MessageChannel getOutput() {
        return this.channelOutputDeals;
    }


    public Flowable<Deal> getFlow() {
        return this.flow;
    }

}
