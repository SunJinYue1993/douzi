package com.github.sunjinyue1993.core.aop;

import com.github.sunjinyue1993.core.config.ZkConnection;
import com.github.sunjinyue1993.core.core.Lock;
import com.github.sunjinyue1993.core.core.zk.DefaultZkLock;
import org.apache.zookeeper.ZooKeeper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 代理方法
 * @author: SunJY
 * @date: 2020/9/15
 */
@Aspect
@Component
public class ServerProxy {

    Logger log = LoggerFactory.getLogger("com.github.sunjinyue1993.core.aop.ServerProxy");

    private final DefaultZkLock lock = new DefaultZkLock();

    @Autowired
    public ZkConnection zkConnection;

    @Pointcut("@annotation(com.github.sunjinyue1993.core.aop.LockAnnotation)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        // 获得连接
        CountDownLatch cc = new CountDownLatch(1);
        ZooKeeper zk = zkConnection.getConnection();
        zkConnection.setGo(cc);
        cc.await(60, TimeUnit.SECONDS);
        log.info("zkConnection status: " + zk.getState());
        lock.setZk(zk);

        // 加锁同步线程
        CountDownLatch goOn = new CountDownLatch(1);
        lock.setGoOn(goOn);
        lock.lock();
        Object result = invocation.proceed();

        // 删除锁
        lock.unLock();
        return result;
    }

}
