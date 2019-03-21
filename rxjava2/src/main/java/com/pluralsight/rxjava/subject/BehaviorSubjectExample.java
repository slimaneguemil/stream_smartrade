package com.pluralsight.rxjava.subject;

import com.pluralsight.rxjava.util.DataGenerator;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class BehaviorSubjectExample {

    public static void main(String[] args) {
        // Create an BehaviorSubject using its factory method
        BehaviorSubject<String> subject = BehaviorSubject.create("Start State");

        // We want to subscribe to this subject
        subject.subscribe(
                (letter) -> {
                    System.out.println(letter);
                }
        );

        // Next we create an observable out of the greek alphabet...
        Observable.from(DataGenerator.generateGreekAlphabet())
                .subscribe(
                        (letter) -> {
                            // ...for each letter, we will emit an event to the subject
                            subject.onNext(letter);
                        },
                        (t) -> {
                            subject.onError(t);
                        },
                        // ...once complete...we tell the subject
                        () -> {
                            System.out.println( "onCompleted" );
                            subject.onCompleted();
                        });

        // Note that we see every event including the start (default) event.
        // BehaviorState is useful for ensuring that a subject always has 
        // a state.
        
        // A second subscriber will see the current event, plus any subsequent
        // events.  In this case, since the subject has reached the completed
        // state, then that is the state that the second subscriber sees.
        subject.subscribe(
                (letter) -> {
                    System.out.println("Second Subscriber: " + letter);
                },
                (t) -> {
                    subject.onError(t);
                },
                () -> {
                    System.out.println( "Second Subscriber: onCompleted" );
                });

        System.exit(0);
    }
}
