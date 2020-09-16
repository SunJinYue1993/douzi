package com.github.sunjinyue1993.core.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultConnectionWatch implements Watcher {

    private CountDownLatch goOn;

    @Override
    public void process(WatchedEvent event) {
        Event.KeeperState state = event.getState();
        switch (state) {
            case SyncConnected:
                System.out.println("Connected...ok...");
                goOn.countDown();
                break;
        }
    }

    public void setGoOn(CountDownLatch goOn) {
        this.goOn = goOn;
    }
}
