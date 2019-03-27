package com.mks;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static org.springframework.cloud.stream.messaging.Source.OUTPUT;

public interface ProducerChannels {


 String OUTPUTBROADCAST = "output";

 @Output(OUTPUTBROADCAST)
 MessageChannel broadcastGreetings();

}
