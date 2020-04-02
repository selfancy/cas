package com.haofei.cas.service;

import com.haofei.cas.dao.BaseDao;

import java.io.Serializable;
import java.util.List;

/**
 * 基础service
 * 提供一些常用的CRUD操作
 *
 * Created by mike on 2020/3/31 since 1.0
 */
public interface BaseService<T, DAO extends BaseDao<T>> {

    /**
     * 获取Dao接口
     */
    default DAO getDao() {
        return ServiceUtil.getDao(this);
    }

    /**
     * 新增
     *
     * @param entity
     */
    default void save(T entity) {
        getDao().save(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    default void update(T entity) {
        getDao().update(entity);
    }

    /**
     * 根据id获取指定数据信息
     *
     * @param id
     * @return
     */
    default T findById(Serializable id) {
        return getDao().findById(id);
    }

    /**
     * 查找全部数据
     *
     * @return
     */
    default List<T> findAll() {
        return getDao().findAll();
    }

    /**
     * 根据id获取检查是否存在
     */
    default boolean existsById(Serializable id) {
        return findById(id) != null;
    }
}
