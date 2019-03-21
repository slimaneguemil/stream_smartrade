package com.mks;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannels {

 String DIRECTED = "directed";
 String BROADCASTS = "broadcasts";
 String POLLABLE = "pollable";

 // <1>
 @Input(DIRECTED)
 SubscribableChannel directed();

 @Input(BROADCASTS)
 SubscribableChannel broadcasts();

/* @Input(POLLABLE )
 PollableMessageSource pollable();*/

}
