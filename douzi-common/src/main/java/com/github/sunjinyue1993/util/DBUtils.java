package com.github.sunjinyue1993.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.github.sunjinyue1993.config.MysqlLockDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class DBUtils implements ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(DBUtils.class);

    private MysqlLockDependency dependency;

    private static DruidDataSource druidDataSource;

    public static DruidPooledConnection getConn() throws SQLException {
        DruidPooledConnection connection = druidDataSource.getConnection();
        return connection;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        dependency = applicationContext.getBean(MysqlLockDependency.class);
        //创建连接池数据源对象
        druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dependency.getDriver());
        druidDataSource.setUrl(dependency.getUrl());
        druidDataSource.setUsername(dependency.getUsername());
        druidDataSource.setPassword(dependency.getPassword());
        //设置连接池策略信息
        druidDataSource.setInitialSize(10); //初始连接数，默认0
        druidDataSource.setMaxActive(30);  //最大连接数，默认8
        druidDataSource.setMinIdle(10);  //最小闲置数
        druidDataSource.setMaxWait(2000);  //获取连接的最大等待时间，单位毫秒
        druidDataSource.setPoolPreparedStatements(true); //缓存PreparedStatement，默认false
        druidDataSource.setMaxOpenPreparedStatements(20);
        log.info("lock datasource: " + dependency.toString());
    }
}





