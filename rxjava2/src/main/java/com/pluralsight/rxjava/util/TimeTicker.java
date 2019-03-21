package com.pluralsight.rxjava.util;

import rx.Observable;
import rx.subjects.PublishSubject;

public class TimeTicker {

    private final PublishSubject<Long> tickerSubject;
    private final long interval;

    private volatile boolean paused;
    private long lastTick;
    private Thread tickerThread;

    public TimeTicker(long interval) {

        tickerSubject = PublishSubject.create();
        tickerThread = null;
        paused = false;
        this.interval = interval;
    }

    public Observable<Long> toObservable() {
        return tickerSubject;
    }

    public synchronized TimeTicker start() {

        if (tickerThread != null) {
            return(this); // the ticker thread is already running.
        }

        lastTick = System.currentTimeMillis();
        
        // make sure to clear the paused flag
        unpause();

        tickerThread = new Thread(() -> {

            try {
                while (Thread.interrupted() == false) {

                    // Sleep for 5 milliseconds
                    Thread.sleep(5);

                    // If we are paused then don't send the ticks.
                    if (paused) {
                        continue;
                    }

                    // Get the current time
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastTick > interval) {
                        lastTick = currentTime;
                        tickerSubject.onNext(lastTick);
                    }
                }
            } catch (InterruptedException e) {
                //suppress
            } catch (RuntimeException t) {
                // Notify all subscribers that there has been an error.
                tickerSubject.onError(t);
            }

            // Make sure all subscribers are told that the list is complete
            tickerSubject.onCompleted();

        }, "TickerThread");
        tickerThread.start();
        
        return this;
    }

    public synchronized TimeTicker stop() {
        if (tickerThread == null) {
            return this; // The ticker thread isn't running.
        }

        tickerThread.interrupt();
        try {
            tickerThread.join();
        } catch (InterruptedException ex) {
            // suppress
        }
        tickerThread = null;
        
        return this;
    }

    public TimeTicker pause() {
        paused = true;
        
        return this;
    }

    public TimeTicker unpause() {
        paused = false;
        
        return this;
    }
}
