package com.mks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.scheduler.Schedulers;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RunAllTests {

    @Test
    public void test1(){
        List<Integer> elements = new ArrayList<>();
        Flux.just(1,2,3,4).log().subscribe(elements::add);
        assertThat(elements).containsExactly(1,2,3,4);
    }

    @Test
    public void test2(){
        List<Integer> elements = new ArrayList<>();
        Flux.just(1,2,3,4).log()
                .map(i -> i*2)
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    int onNextAmount;


                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(2);
                    }

                    @Override
                    public void onNext(final Integer integer) {
                        elements.add(integer);
                        onNextAmount++;
                        s.request(2);
                    }

                    @Override
                    public void onError(final Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        int ham = 2;
                    }
                });

        assertThat(elements).containsExactly(2,4,6,8);
    }


    @Test
    public void givenFlux_whenApplyingBackPressure_thenPushElementsInBatches() throws InterruptedException {

        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .log()
                .map(i -> i * 2)
                .onBackpressureBuffer()
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    int onNextAmount;

                    @Override
                    public void onSubscribe(final Subscription s) {
                        this.s = s;
                        s.request(2);
                    }

                    @Override
                    public void onNext(final Integer integer) {
                        elements.add(integer);
                        onNextAmount++;
                        if (onNextAmount % 2 == 0) {
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(final Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        int ham = 2;
                    }
                });

        assertThat(elements).containsExactly(2, 4, 6, 8);
    }

    @Test
    public void Connectableflux_cold(){
        List<Integer> elements = new ArrayList<>();
       ConnectableFlux<Integer> publish = Flux.just(1, 2, 3, 4).publish();
        publish.log().subscribe(elements::add);
        assertThat(elements).isEmpty();
        publish.connect();
        assertThat(elements).containsExactly(1,2,3,4);
    }

    @Test
    public void Connectableflux_hot(){
        List<Integer> elements = new ArrayList<>();
        ConnectableFlux<Object> publish =
                Flux.create( fluxSink ->
                        {
                           while(true){
                               fluxSink.next(System.currentTimeMillis());
                           }
                        }
                        )
                .publish();


        publish.log().subscribe(System.out::println);
        assertThat(elements).isEmpty();
        publish.connect();
       // assertThat(elements).containsExactly(1,2,3,4);
    }

}
