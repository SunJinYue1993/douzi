package com.github.sunjinyue1993.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * tbl_mysql_lock
 * @author 
 */
@Data
public class TblMysqlLock implements Serializable {
    private Integer lockId;

    private Integer lockStatus;

    private static final long serialVersionUID = 1L;
}