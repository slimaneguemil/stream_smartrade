package notused;

import com.mks.ConsumerChannels;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

//@Component
class Consuming {

    private Log log = LogFactory.getLog(getClass());

    //@StreamListener(ConsumerChannels.DIRECTED)
    public void onNewDirectedGreetings(String greeting) {
        System.out.println("directed");
        this.onNewGreeting(ConsumerChannels.DIRECTED, greeting);
    }

    //@StreamListener(ConsumerChannels.BROADCASTS)
    public void onNewBroadcastGreeting(String greeting) {
        System.out.println("broadcasting");
        this.onNewGreeting(ConsumerChannels.BROADCASTS, greeting);
    }

    private void onNewGreeting(String prefix, String greeting) {
        System.out.println("greeting received in @StreamListener (" + prefix + "): "
                + greeting);
    }
}