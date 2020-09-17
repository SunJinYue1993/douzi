package com.github.sunjinyue1993.core.core.zk;

import com.github.sunjinyue1993.core.config.ZkConnection;
import com.github.sunjinyue1993.core.core.ZkLock;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractZkLock implements ZkLock, Watcher, AsyncCallback.StringCallback,
        AsyncCallback.Children2Callback, AsyncCallback.StatCallback {

    Logger log = LoggerFactory.getLogger("com.github.sunjinyue1993.core.core.zk.DefaultZkLock");

    ZooKeeper zk;

    String pathName;

    CountDownLatch cc = new CountDownLatch(1);

    @Autowired
    private ZkConnection conn;

    @Override
    public void lock() {
        log.info("start get zkConn...");
        zk = conn.zkConnection();
        try {
            log.info("Thread create id: " + Thread.currentThread().getName());
            this.zk.create("/lock", Thread.currentThread().getName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL, this, "abc");
            log.error("ServerProxy" + Thread.currentThread().getName() + "," + Thread.currentThread().getId());
            // 所有线程请求创建节点，进去等待状态，zooleeper返回成功唤起自己线程，进入回调
            cc.await();
            log.info("zk status: " + this.zk.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // StringCallBack
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        log.info("StringCallBack thead id: " + Thread.currentThread().getId());
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
        log.info("Children2Callback thead id: " + Thread.currentThread().getId());
        Collections.sort(children);
        int i = children.indexOf(pathName.substring(1));
        //是不是第一个
        if (i == 0) {
            log.info("The first thread: " + Thread.currentThread().getName());
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
            case None:
                log.info("None");
                break;
            case NodeCreated:
                log.info("NodeCreated");
                break;
            case NodeDeleted:
                log.info("NodeDeleted");
                zk.getChildren("/", false, this, "sdf");
                break;
            case NodeDataChanged:
                log.info("NodeDataChanged");
                break;
            case NodeChildrenChanged:
                log.info("NodeChildrenChanged");
                break;
        }
    }

    @Override
    public void unLock() {
        try {
            log.info("unLock thead id: " + Thread.currentThread().getId());
            zk.delete(pathName, -1);
            log.info(Thread.currentThread().getName() + " over work....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
