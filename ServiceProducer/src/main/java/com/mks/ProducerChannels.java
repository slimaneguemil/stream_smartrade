package com.mks;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

//@EnableBinding(ProducerChannels.class)
public interface ProducerChannels {

 // <1>
 String DIRECT = "directGreetings";

 String BROADCAST = "broadcastGreetings";

 @Output(DIRECT)
 // <2>
 MessageChannel directGreetings();

 @Output(BROADCAST)
 MessageChannel broadcastGreetings();
}
