package com.github.sunjinyue1993.core.core.zk;

import com.github.sunjinyue1993.core.core.ZkLock;
import com.github.sunjinyue1993.core.entity.DefaultConnectionWatch;
import com.github.sunjinyue1993.core.entity.LockDependency;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractZkLock implements ZkLock, Watcher, AsyncCallback.StringCallback,
        AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    Logger log = LoggerFactory.getLogger("com.github.sunjinyue1993.core.core.zk.DefaultZkLock");

    ZooKeeper zk;

    ReentrantLock lock = new ReentrantLock();

    String pathName;

    @Autowired
    @Qualifier("defaultConnectionWatch")
    private DefaultConnectionWatch watch;

    CountDownLatch cc = new CountDownLatch(1);
    CountDownLatch goOn = new CountDownLatch(1);

    @Autowired
    private ApplicationContext ac;

    @Override
    public void lock() {
        lock.lock();
        try{
            System.out.println("start.....");
            watch.setGoOn(goOn);
            zk = (ZooKeeper) ac.getBean("zkConnection");
            goOn.await();
            System.out.println("end.....");
        } catch (Exception e) {
            log.error("get zkConnection error");
        } finally {
            lock.unlock();
        }

        try {
            System.out.println(Thread.currentThread().getName() + "-->>  create....");
            zk.create("/lock", Thread.currentThread().getName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL, this, "abc");
            // 所有线程请求创建节点，进去等待状态，zooleeper返回成功唤起自己线程，进入回调
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // StringCallBack
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name != null) {
            System.out.println(Thread.currentThread().getName() + "  create node : " + name);
            pathName = name;
            // 这是的根节点是/testLock
            zk.getChildren("/", false, this, "sdf");
        }
    }

    // Children2Callback
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {

        Collections.sort(children);
        //TODO：如果这个节点(\00000022)排在集合里排第一个 -> pathName.substring(1) = 00000022
        int i = children.indexOf(pathName.substring(1));

        //是不是第一个
        if (i == 0) {
            //yes
            System.out.println(Thread.currentThread().getName() + " i am first....");
            try {
                zk.setData("/", Thread.currentThread().getName().getBytes(), -1);
                cc.countDown();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            //不是第一个，那么监控他前边那一个，前边那个释放锁会删除字节的节点(锁)，一旦删除了重新竞争锁（在上面删除节点事件监听）
            zk.exists("/" + children.get(i - 1), this, this, "sdf");
        }
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case NodeDeleted:
                zk.getChildren("/", false, this, "sdf");
                break;
        }
    }

    @Override
    public void unLock() {
        try {
            zk.delete(pathName, -1);
            System.out.println(Thread.currentThread().getName() + " over work....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }

}
