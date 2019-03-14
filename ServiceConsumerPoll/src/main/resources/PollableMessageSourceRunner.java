import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PollableMessageSourceRunner {

    private ConsumerChannels customProcessor;

    @Autowired //Always, always, always autowire via constructor
    public PollableMessageSourceRunner(ConsumerChannels customProcessor) {
        this.customProcessor = customProcessor;
    }

    //Every 1000ms this method will be executed
    @Scheduled(fixedDelayString = "1000")
    public void pollMessages() {
        Log log = LogFactory.getLog(getClass());
        boolean hasMessage = true;
        log.info("listening for incoming messages ....");
        //checking if service is healthy
            while (hasMessage) {
                //we are pulling message by message from the topic,
                //if topic empty we stop

                hasMessage = customProcessor.pollable().poll(
                         message -> {
                             //send our message to external service
                            log.info("polling"+message);
                         }
                 );

            }
        }

}