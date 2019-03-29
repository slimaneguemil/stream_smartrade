package com.mks;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannels {


 String INPUTBROADCASTS = "input";

 @Input(INPUTBROADCASTS)
 SubscribableChannel broadcasts();


}
