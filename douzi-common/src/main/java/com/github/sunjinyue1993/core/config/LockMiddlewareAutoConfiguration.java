package com.github.sunjinyue1993.core.config;

import com.github.sunjinyue1993.core.entity.LockDependency;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties(LockDependency.class)
@ComponentScan(basePackages = "com.github.sunjinyue1993.core.core.*")
public class LockMiddlewareAutoConfiguration {

}
