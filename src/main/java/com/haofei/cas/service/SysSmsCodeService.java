package com.haofei.cas.service;

import com.haofei.cas.dao.SysSmsCodeDao;
import com.haofei.cas.entity.SysSmsCode;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
public interface SysSmsCodeService extends BaseService<SysSmsCode, SysSmsCodeDao> {

    SysSmsCode getSysSmsCodeBy(String mobile, Integer opType);
}
