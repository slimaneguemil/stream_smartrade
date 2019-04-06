package com.mks;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.MessageChannel;

@EnableBinding(Channels.PolledProcessor.class)
public class Channels {

    public interface PolledProcessor {

        @Input
        PollableMessageSource input();

        @Output
        MessageChannel output();

    }
}
