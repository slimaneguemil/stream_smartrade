package com.mks.utils;

import java.util.concurrent.ThreadLocalRandom;

public class utils {
    private static final long start = System.currentTimeMillis();

    static Data getData() {
        Data data = new Data();
        data.setId(1L);
        data.setName("getData");
        data.setAmount(ThreadLocalRandom.current().nextDouble());
        return data;
    }

    static Data getData(Long id) {
        Data data = new Data();
        data.setId(id);
        data.setName("getData-id");
        data.setAmount(ThreadLocalRandom.current().nextDouble());
        return data;
    }

    static Data getData(Long id, String name) {
        Data data = new Data();
        data.setId(id);
       data.setName(name);
        data.setAmount(ThreadLocalRandom.current().nextDouble());
        return data;
    }

    public static Deal getDeal(Long id, String name, long time, String version) {
        Deal deal = new Deal();
        deal.setStart(time);
        deal.setData(getData(id, name));
        deal.setVersion(version);
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
