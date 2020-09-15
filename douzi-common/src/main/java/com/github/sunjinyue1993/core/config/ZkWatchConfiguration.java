package com.github.sunjinyue1993.core.config;

import com.github.sunjinyue1993.core.entity.DefaultConnectionWatch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Priority;

/**
 * 配置注册到zk的Watch
 *
 * @author: SunJY
 * @date: Created in 2020/9/14 下午 8:56
 */
@Configuration
public class ZkWatchConfiguration {

    @Bean
    public DefaultConnectionWatch getWatch() {
        return new DefaultConnectionWatch();
    }

}
