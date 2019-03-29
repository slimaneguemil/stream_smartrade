package com.mks;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ProducerChannels {


 String OUTPUTBROADCAST = "output";

 @Output(OUTPUTBROADCAST)
 MessageChannel broadcastGreetings();

}
