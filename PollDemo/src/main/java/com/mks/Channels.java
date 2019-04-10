package com.mks;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.MessageChannel;

@EnableBinding(Channels.PolledProcessor.class)
public class Channels {

    public interface PolledProcessor {

        static String INPUT2="input2";
        static String OUTPUT2="output2";

        //for Deals
        @Input
        PollableMessageSource input();
        @Output
        MessageChannel output();

        //for System:logs
        @Input(INPUT2)
        PollableMessageSource input2();
        @Output(OUTPUT2)
        MessageChannel output2();


    }
}
