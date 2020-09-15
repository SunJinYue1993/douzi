package com.github.sunjinyue1993.core.aop;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: admin
 * @date: Created in 2020/9/14 下午 11:31
 */
@Component
public class ServerProxy {

    @Autowired
    private ApplicationContext zkConnection;

    public ZooKeeper getZkConnect(){
        return (ZooKeeper) zkConnection.getBean("zkConnection");
    }

}
