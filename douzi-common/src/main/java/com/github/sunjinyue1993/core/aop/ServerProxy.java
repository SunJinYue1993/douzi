package com.github.sunjinyue1993.core.aop;

import com.github.sunjinyue1993.core.config.ZkConnection;
import com.github.sunjinyue1993.core.core.Lock;
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

/**
 *
 * @author: SunJY
 * @date: 2020/9/15
 */
@Component
@Aspect
public class ServerProxy {

    Logger log = LoggerFactory.getLogger("com.github.sunjinyue1993.core.aop.ServerProxy");

    @Autowired
    private Lock lock;

    @Autowired
    public ZkConnection zkConnection;

    private CountDownLatch cc = new CountDownLatch(1);

    @Pointcut("@annotation(com.github.sunjinyue1993.core.aop.LockAnnotation)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        // 获得连接
        ZooKeeper zk = zkConnection.zkConnection();
        zkConnection.setGo(cc);
        cc.await();
        lock.lock();
        // 异步
        Object result = invocation.proceed();
        lock.unLock();
        return result;
    }

}
