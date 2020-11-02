package com.github.sunjinyue1993.core.mysql;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.github.sunjinyue1993.core.Lock;
import com.github.sunjinyue1993.core.redis.RedisLock;
import com.github.sunjinyue1993.entity.TblMysqlLock;
import com.github.sunjinyue1993.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@ConditionalOnMissingBean(RedisLock.class)
public class MysqlLock implements Lock {

    private static Logger log = LoggerFactory.getLogger(MysqlLock.class);

    private ThreadLocal<TblMysqlLock> threadLocal;

    @Override
    public void lock() {
        // 1、尝试加锁
        if (tryLock()) {
            return;
        }
        //TODO: 2.休眠 -> 考虑AQS的CHL队列,阻塞加锁
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            log.error("线程: " + Thread.currentThread().getName() + "意外打断!");
        }
        // 3.递归再次调用
        lock();
    }

    /**
     * 非阻塞式加锁，成功，就成功，失败就失败。直接返回
     */
    public boolean tryLock() {
        DruidPooledConnection conn = null;
        try {
            conn = DBUtils.getConn();
            conn.createStatement().executeUpdate("insert into tbl_mysql_lock values (1, 1)");
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("线程: " + Thread.currentThread().getName() + "尝试加锁异常");
            }
        }
        return true;
    }

    @Override
    public void unlock() {
        DruidPooledConnection conn = null;
        try {
            conn = DBUtils.getConn();
            conn.createStatement().executeUpdate("delete from tbl_mysql_lock where lock_id = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("线程: " + Thread.currentThread().getName() + "释放锁异常");
            }
        }
//        threadLocal.remove();
    }

    public void setThreadLocal(ThreadLocal<TblMysqlLock> threadLocal) {
        this.threadLocal = threadLocal;
    }

}
