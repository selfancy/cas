package com.haofei.cas.service;

import com.haofei.cas.dao.SysUserDao;
import com.haofei.cas.entity.SysUser;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
public interface SysUserService extends BaseService<SysUser, SysUserDao> {

    SysUser findByUsername(String username);

    SysUser checkMobileAndGetUser(String username);
}
