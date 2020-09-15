package com.github.sunjinyue1993.core.core.zk;

import com.github.sunjinyue1993.core.core.ZkLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractZkLock implements ZkLock {

    Logger log = LoggerFactory.getLogger("com.github.sunjinyue1993.core.core.zk.DefaultZkLock");

    @Override
    public void lock() {
        log.debug("zkLock start...");
    }

    @Override
    public void unLock() {
        log.debug("zkLock end...");
    }

}
