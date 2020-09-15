package com.github.sunjinyue1993.core.core;

/**
 * 顶级接口，用于分布式锁的扩展，默认支持zk锁
 *
 * @author: SunJY
 * @date: 2020/9/15
 * @see com.github.sunjinyue1993.core.aop.ServerProxy
 */
public interface Lock {

    void lock();

    void unLock();

}
