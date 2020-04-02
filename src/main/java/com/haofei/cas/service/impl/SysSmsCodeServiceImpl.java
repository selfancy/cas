package com.haofei.cas.service.impl;

import com.haofei.cas.entity.SysSmsCode;
import com.haofei.cas.service.SysSmsCodeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
@Service
public class SysSmsCodeServiceImpl implements SysSmsCodeService {

    @Override
    public void save(SysSmsCode entity) {
        entity.setCreatedTime(new Date());
        entity.setUpdatedTime(new Date());
        getDao().save(entity);
    }

    @Override
    public void update(SysSmsCode entity) {
        entity.setUpdatedTime(new Date());
        getDao().update(entity);
    }

    @Override
    public SysSmsCode getSysSmsCodeBy(String mobile, Integer opType) {
        List<SysSmsCode> list = getDao().getSysSmsCodeBy(mobile, opType);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }
}
