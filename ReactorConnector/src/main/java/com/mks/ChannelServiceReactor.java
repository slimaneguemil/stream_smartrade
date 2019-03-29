package com.mks;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Component
public class ChannelServiceReactor {
    Log log = LogFactory.getLog(getClass());
    private ObjectMapper mapper;
    private final MessageChannel broadcast;
    Flux<Foo> flux;
    FluxSink<Foo> webSocketCommentSink;

    ChannelServiceReactor(ObjectMapper mapper, ProducerChannels channels) {
        this.broadcast = channels.broadcastGreetings();
        //this.direct = channels.directGreetings();

        this.mapper = mapper;
        this.flux = Flux.<Foo>create(
                emitter -> this.webSocketCommentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
      //  this.flux.subscribe();
    }

    public void publish(Foo payload){
       // this.direct.send(MessageBuilder.withPayload(payload).build());
        this.broadcast.send(MessageBuilder.withPayload(payload).build());
    }


    //@StreamListener(ConsumerChannels.BROADCASTS)
    private void subscribeDirect(Foo message) {
        log.info("extends class:" + message);
        if (webSocketCommentSink != null) {
         log.info("Publishing " + message.toString() +
                    " to websocket...");
            this.webSocketCommentSink.next(message);
        }

    }

}
