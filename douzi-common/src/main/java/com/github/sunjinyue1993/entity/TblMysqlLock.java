package com.github.sunjinyue1993.entity;

import java.io.Serializable;

public class TblMysqlLock implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer lockId;

    private Integer lockStatus;


    public Integer getLockId() {
        return lockId;
    }

    public void setLockId(Integer lockId) {
        this.lockId = lockId;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }
}
