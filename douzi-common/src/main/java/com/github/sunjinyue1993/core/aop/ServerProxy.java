package com.github.sunjinyue1993.core.aop;

import com.github.sunjinyue1993.core.config.ZkConnection;
import com.github.sunjinyue1993.core.core.zk.DefaultZkLock;
import org.apache.zookeeper.ZooKeeper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * 代理方法
 * @author: SunJY
 * @date: 2020/9/15
 */
@Aspect
@Component
public class ServerProxy {

    private static Logger log = LoggerFactory.getLogger(ServerProxy.class);

    @Autowired
    public ZkConnection zkConnection;

    @Pointcut("@annotation(com.github.sunjinyue1993.core.aop.LockAnnotation)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        log.info("ServerProxy invoke() -> thread id: " + Thread.currentThread().getId());
        DefaultZkLock lock = new DefaultZkLock();
        // 获得连接
        CountDownLatch cc = new CountDownLatch(1);
        ZooKeeper zk = zkConnection.getConnection();
        zkConnection.setGo(cc);
        cc.await();
        log.info("ServerProxy invoke() zkConnection status: " + zk.getState());
        lock.setZk(zk);

        // 加锁同步线程
        lock.lock();
        Object result = invocation.proceed();

        // 删除锁
        lock.unLock();
        return result;
    }

}
