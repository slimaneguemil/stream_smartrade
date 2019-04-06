package com.mks.utils;

import com.mks.Foo;

import java.util.concurrent.ThreadLocalRandom;

public class utils {
    private static final long start = System.currentTimeMillis();

    static Foo getFoo(){
        Foo foo = new Foo();
        foo.setId(1);
        foo.setName("getfoo");
        foo.setAmount(ThreadLocalRandom.current().nextDouble());
        return foo;
    }

    static Foo getFoo(int id){
        Foo foo = new Foo();
        foo.setId(id);
        foo.setName("getfoo-id");
        foo.setAmount(ThreadLocalRandom.current().nextDouble());
        return foo;
    }

    static Foo getFoo(int id, String name) {
        Foo foo = new Foo();
        foo.setId(id);
        foo.setName(name);
        foo.setAmount(ThreadLocalRandom.current().nextDouble());
        return foo;
    }

        public static Message getMessage(int id, String name, long time){
        Message message = new Message();
        message.setStart(time);
        message.setData(getFoo(id,name));
        return message;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static <T>T intenseCalculation(T value){
        sleep(ThreadLocalRandom.current().nextInt(2000));
        return  value;
    }

    public static void log(Object label){
        System.out.println(
                System.currentTimeMillis() - start +"\t|" +
                Thread.currentThread().getName()  +"\t|" +
                label
                );
    }

}
