package com.github.sunjinyue1993.core.config;

import com.github.sunjinyue1993.core.entity.LockDependency;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 配置连接,并获得已经连接好的ZkConnection
 *
 * @author: SunJY
 * @date: Created in 2020/9/14 下午 8:46
 */
@Component
public class ZkConnection implements ApplicationContextAware {

    Logger log = LoggerFactory.getLogger("com.github.sunjinyue1993.core.config.ZkConnection");

    private LockDependency ld;

    private CountDownLatch go;

    public ZooKeeper getConnection() {
        ZooKeeper zk = null;
        try {
            log.info("zkConnection thead id: " + Thread.currentThread().getId());
            zk = new ZooKeeper(ld.getMiddleware().getAddress(), 7000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    log.info("process thead id: " + Thread.currentThread().getId());
                    switch (event.getState()) {
                        case SyncConnected:
                            go.countDown();
                            break;
                    }
                }
            });
            log.info("get zkConn status:" + zk.getState());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zk;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        ld = ac.getBean(LockDependency.class);
    }

    public void setGo(CountDownLatch go) {
        this.go = go;
    }
}
