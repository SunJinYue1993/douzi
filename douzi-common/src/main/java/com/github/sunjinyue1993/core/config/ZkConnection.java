package com.github.sunjinyue1993.core.config;

import com.github.sunjinyue1993.core.entity.DefaultConnectionWatch;
import com.github.sunjinyue1993.core.entity.LockDependency;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 配置连接,并获得已经连接好的ZkConnection
 *
 * @author: SunJY
 * @date: Created in 2020/9/14 下午 8:46
 */
@Configuration
public class ZkConnection implements ApplicationContextAware {

    private LockDependency ld;

    private DefaultConnectionWatch watch;

    private CountDownLatch count = new CountDownLatch(1);

    @Bean
    @Scope("prototype")
    public ZooKeeper zkConnection() {
        watch.setInit(count);
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(ld.getMiddleware().getAddress(), 1000, watch);
            count.await();
        } catch (IOException | InterruptedException e) {

        }
        return zk;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        ld = ac.getBean(LockDependency.class);
        watch = ac.getBean(DefaultConnectionWatch.class);
    }
}
