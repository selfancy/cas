package com.haofei.cas.dao;

import com.haofei.cas.entity.SysSmsCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
public interface SysSmsCodeDao extends BaseDao<SysSmsCode> {

    List<SysSmsCode> getSysSmsCodeBy(@Param("mobile") String mobile, @Param("opType") Integer opType);
}
