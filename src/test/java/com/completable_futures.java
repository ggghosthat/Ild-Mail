package com;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//tryna get understand working process of completable futures :D

public class completable_futures {
    private static test1 test1 = new test1();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> result = CompletableFuture.runAsync(test1);
        System.out.println("step here");
        result.get();
    }
}

class test1 implements  Runnable{

    @Override
    public void run() {

        try {
            Thread.sleep(10000);
            System.out.println("this is cool java code !!!");
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
