package com.mks.utils;

import java.util.concurrent.ThreadLocalRandom;

public class utils {
    private static final long start = System.currentTimeMillis();

    static Data getData() {
        Data Data = new Data();
        Data.setId(1L);
        Data.setName("getData");
        Data.setAmount(ThreadLocalRandom.current().nextDouble());
        return Data;
    }

    static Data getData(Long id) {
        Data Data = new Data();
        Data.setId(id);
        Data.setName("getData-id");
        Data.setAmount(ThreadLocalRandom.current().nextDouble());
        return Data;
    }

    static Data getData(Long id, String name) {
        Data Data = new Data();
        Data.setId(id);
        Data.setName(name);
        Data.setAmount(ThreadLocalRandom.current().nextDouble());
        return Data;
    }

    public static Deal2 getDeal(Long id, String name, long time) {
        Deal2 deal = new Deal2();
        deal.setName("dede");
        deal.setStart(time);
        deal.setData(getData(id, name));
        return deal;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int sleepRandom(int millis) {

        return ThreadLocalRandom.current().nextInt(millis);

    }

    public static <T> T intenseCalculation(T value) {
        sleep(ThreadLocalRandom.current().nextInt(2000));
        return value;
    }

    public static void log(Object label) {
        System.out.println(
                System.currentTimeMillis() - start + "\t|" +
                        Thread.currentThread().getName() + "\t|" +
                        label
        );
    }

}
