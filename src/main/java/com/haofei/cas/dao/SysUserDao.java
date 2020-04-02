package com.haofei.cas.dao;

import com.haofei.cas.entity.SysUser;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
public interface SysUserDao extends BaseDao<SysUser> {

    SysUser findByUsername(String username);
}
