package com.github.sunjinyue1993.core.core.zk;

import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@Priority(value = 3)
public class DefaultZkLock extends AbstractZkLock {

    @Override
    public void other() {

    }

}
