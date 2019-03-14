package com.mks;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannels {

 String POLLABLE = "pollable";

@Input(POLLABLE )
PollableMessageSource pollable();

}
