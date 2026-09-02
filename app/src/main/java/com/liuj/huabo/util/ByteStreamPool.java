package com.liuj.huabo.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteStreamPool {
    private static ConcurrentLinkedQueue<ByteStreamWrapper> pool = new ConcurrentLinkedQueue<>();
    private static AtomicInteger created = new AtomicInteger(0);
    private static AtomicInteger borrowed = new AtomicInteger(0);
    private static AtomicInteger returned = new AtomicInteger(0);
    public static ByteStreamWrapper get() {
        ByteStreamWrapper result = pool.poll();
        if (result == null) {
            result = new ByteStreamWrapper();
            created.incrementAndGet();
        }
        borrowed.incrementAndGet();
        return result;
    }
    public static void ret2pool(ByteStreamWrapper result){
        result.reset();
        pool.add(result);
        returned.incrementAndGet();
    }
    public static String stats(){
        return "created:" + created.get() + ",borrowed:" + borrowed.get() + ",returned:" + returned.get();
    }
}