# 使用手册
### 1. 添加依赖

```xml
<dependency>
    <groupId>com.github.sunjinyue1993</groupId>
    <artifactId>douzi-spring-boot-starter</artifactId>
    <version>1.0.1.RELEASE</version>
</dependency>
```

- 基于MySQL

```markdown
- 创建表
create table tbl_mysql_lock
(
    lock_id     int null,
    lock_status int null
);
create index tbl_mysql_lock_lock_id_index on tbl_mysql_lock (lock_id);
```

```java
- 在需要同步的方法上添加@MysqlLock注解,默认模式
@MysqlLock
public String syncMethod() {
    // 抢单。。。。
}
```

- 基于单机Redis

```java
- 配置RedisLock
@Configuration
public class LockConfig {
    @Bean
    public Lock lock() {
        return new RedisLock();
    }
}
- 在需要同步的方法上添加@RedisLock注解
@RedisLock
public String syncMethod() {
    // 抢单。。。。
}

```

