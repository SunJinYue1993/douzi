package com.github.sunjinyue1993.mapper;

import com.github.sunjinyue1993.entity.TblMysqlLock;

public interface TblMysqlLockDao {
    int deleteByPrimaryKey(Integer lockId);

    int insert(TblMysqlLock record);

    int insertSelective(TblMysqlLock record);

    TblMysqlLock selectByPrimaryKey(Integer lockId);

    int updateByPrimaryKeySelective(TblMysqlLock record);

    int updateByPrimaryKey(TblMysqlLock record);
}