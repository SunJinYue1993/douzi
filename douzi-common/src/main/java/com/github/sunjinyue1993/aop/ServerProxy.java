package com.github.sunjinyue1993.aop;

import com.github.sunjinyue1993.core.mysql.MysqlLock;
import com.github.sunjinyue1993.entity.TblMysqlLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 代理方法
 *
 * @author: SunJY
 * @date: 2020/9/15
 */
@Aspect
@Component
public class ServerProxy {

    private static Logger log = LoggerFactory.getLogger(ServerProxy.class);

    private MysqlLock lock = new MysqlLock();

    private ThreadLocal<TblMysqlLock> threadLocal = new ThreadLocal<>();

    @Pointcut("@annotation(com.github.sunjinyue1993.lockannotation.MysqlLockAnnotation)")

    public void pointCut(){}

    @Around("pointCut()")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        log.info("ServerProxy invoke() -> thread id: " + Thread.currentThread().getId());
//        TblMysqlLock tblMysqlLock = new TblMysqlLock();
//        tblMysqlLock.setLockId(1);
//        tblMysqlLock.setLockStatus(1);
//        threadLocal.set(tblMysqlLock);
//        lock.setThreadLocal(threadLocal);

        // 加锁同步线程
        lock.lock();

        Object result = invocation.proceed();

        // 删除锁
        lock.unlock();
        return result;
    }
}
