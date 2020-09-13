package com.github.sunjinyue1993.common.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lock.dependency")
public class LockDependency {

    /**
     * 锁依赖中间件.
     */
    private String middleware;

    /**
     * 是否开启集群模式.
     */
    private String enable;

//    /**
//     * 中间件IP地址.
//     */
//    private String ip = "127.0.0.1";
//
//    /**
//     * IP端口号.
//     */
//    private int port = 9797;


    public String getMiddleware() {
        return middleware;
    }

    public void setMiddleware(String middleware) {
        this.middleware = middleware;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
