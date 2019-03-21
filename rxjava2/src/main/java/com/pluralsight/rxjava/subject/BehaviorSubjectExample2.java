package com.pluralsight.rxjava.subject;

import com.pluralsight.rxjava.util.DataGenerator;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class BehaviorSubjectExample2 {

    public static void main(String[] args) {
        // Create an BehaviorSubject using its factory method
        BehaviorSubject<String> subject = BehaviorSubject.create("Start State");

        // We want to subscribe to this subject
        subject.subscribe(
                (letter) -> {
                    System.out.println(letter);
                }
        );
        subject.subscribe(
                (letter) -> {
                    System.out.println("Second Subscriber: " + letter);
                });


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

        // Note that we see every event on both subscribers.  Subjects are 
        // an easy way to "multicast" events to multiple subscribers.
        // A Subject is both an Observable and a Subscriber at the same time.
        
        System.exit(0);
    }
}
