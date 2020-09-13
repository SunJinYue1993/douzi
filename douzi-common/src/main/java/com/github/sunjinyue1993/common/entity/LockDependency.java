package com.github.sunjinyue1993.common.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 配置存储锁的中间件连接属性
 * https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/html/appendix-configuration-metadata.html#configuration-metadata-annotation-processor-metadata-generation
 * @author: sunjy
 * @date: 2020/9/13 下午 11:35
 */
@ConfigurationProperties(prefix = "lock.dependency")
public class LockDependency {

    private Middleware middleware;

    private Cluster cluster;

    public static class Middleware {

        /**
         * 锁依赖中间件.
         */
        private String name;

        /**
         * 中间件IP地址.
         */
        private String address;

        /**
         * IP端口号.
         */
        private String port;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }

    public static class Cluster {

        /**
         * 是否开启集群模式.
         */
        private boolean enable;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    public Middleware getMiddleware() {
        return middleware;
    }

    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

}
