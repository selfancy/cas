package com.haofei.cas.dao;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础Dao
 *
 * Created by mike on 2020/3/31 since 1.0
 */
public interface BaseDao<T> {

    /**
     * 新增保存
     */
    void save(T entity);

    /**
     * 批量保存
     */
    void saveAll(@Param("list") Collection<T> entities);

    /**
     * 修改
     */
    void update(T entity);

    /**
     * 根据id查找
     */
    T findById(Serializable id);

    /**
     * 查找全部
     */
    List<T> findAll();
}