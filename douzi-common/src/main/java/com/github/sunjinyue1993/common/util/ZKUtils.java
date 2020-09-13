package com.github.sunjinyue1993.common.util;

import com.github.sunjinyue1993.common.config.DefaultWatch;
import com.github.sunjinyue1993.common.entity.LockDependency;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
/**
 * @description:
 * TODO: 连接是否可以多人共享，还是独享Watch？ 待验证
 *
 * @author: sunjy
 * @date: 2020/9/14 上午 12:08
 */
@Component
public class ZKUtils {
    private ZooKeeper zk;

    @Autowired
    private LockDependency conf;

    @Autowired
    private DefaultWatch watch;

    private CountDownLatch c = new CountDownLatch(1);


    public ZooKeeper getZK() {
        try {
            zk = new ZooKeeper(conf.getMiddleware().getAddress(), 1000, watch);
            c.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }

    public void closeZK() {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
