package com.github.sunjinyue1993.common.config;

import com.github.sunjinyue1993.common.entity.LockDependency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LockDependency.class)
public class LockMiddlewareAutoConfiguration {

    @Autowired
    private LockDependency lockDependency;

    public String print() {
        return lockDependency.getEnable() + "====" + lockDependency.getMiddleware();
    }

}
