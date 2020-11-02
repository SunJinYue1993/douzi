package com.github.sunjinyue1993.core.redis;

import com.github.sunjinyue1993.core.Lock;

public class RedisLock implements Lock {
    @Override
    public void lock() {
        System.out.printf("lock()... ");
    }

    @Override
    public void unlock() {
        System.out.printf("unlock()... ");
    }
}
