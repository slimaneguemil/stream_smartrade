package com.pluralsight.rxjava.connectable;

import com.pluralsight.rxjava.util.ThreadUtils;
import com.pluralsight.rxjava.util.TimeTicker;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

public class SlowParallelObserveOnConnectableExample {

    public static void main(String[] args) {

        // Create a ticker that goes off every 1/2 second.
        TimeTicker ticker = new TimeTicker(500).start();
        
        // From the ticker, we create a connectable observable by 
        // get the observable and then calling its "publish" method.
        ConnectableObservable<Long> connectable = ticker
                .toObservable()
                .publish();

        // Next we do our subscription....
        connectable
                // We want to run this on a different thread than the ticker thread
                .observeOn(Schedulers.computation())
                .subscribe(
                        (t) -> {
                            System.out.println("Tick: " + ThreadUtils.currentThreadName() + " " + t);
                        }
                );
        
        connectable
                // We want to run this on a different thread than the ticker thread
                .observeOn(Schedulers.computation())
                .subscribe(
                        (t) -> {
                            System.out.println("Tick2: " + ThreadUtils.currentThreadName() + " " + t);
                            ThreadUtils.sleep(1000);
                        }
                );

        // But notice how for 3 seconds nothing happens until we call "connect"
        System.out.println("Sleeping for 3 seconds...");
        ThreadUtils.sleep(3000);

        // Now we call "connect" on the connectable observable...and things start happening...
        System.out.println("Connecting...");
        connectable.connect();

        // Let the ticker do its thing for 3 seconds so we can see events
        // flowing...
        // One of our observers is slow...see what happens...
        ThreadUtils.sleep(5000);
        System.out.println("Five seconds are up! STOPPING THE TICKER");
        
        // Stop the ticker and kill the example's VM
        ticker.stop();
        
        // Wait another few seconds to let us catch up...
        ThreadUtils.sleep(5000);
        
        System.out.println( "Notice how the second observer continued to process its scheduled work...");
        System.exit(0);
    }
}
