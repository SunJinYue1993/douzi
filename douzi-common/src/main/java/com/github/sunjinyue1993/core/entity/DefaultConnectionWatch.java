package com.github.sunjinyue1993.core.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class DefaultConnectionWatch implements Watcher {

    private CountDownLatch init;

    @Override
    public void process(WatchedEvent event) {
        Event.KeeperState state = event.getState();
        switch (state) {
            case SyncConnected:
                System.out.println("Connected...ok...");
                init.countDown();
                break;
        }
    }

    public void setInit(CountDownLatch init) {
        this.init = init;
    }
}
