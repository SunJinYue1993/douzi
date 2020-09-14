package com.github.sunjinyue1993.core.zklock;

import com.github.sunjinyue1993.core.entity.DefaultWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZkLock {

    @Autowired
    private DefaultWatch defaultWatch;

    public DefaultWatch getDefaultWatch() {
        return defaultWatch;
    }

}
