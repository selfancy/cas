package com.haofei.cas.service.impl;

import com.google.common.base.Strings;
import com.haofei.cas.entity.SysUser;
import com.haofei.cas.exception.BizException;
import com.haofei.cas.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private static final Pattern mobilePattern = Pattern.compile("^1\\d{10}$");

    @Override
    public SysUser findByUsername(String username) {
        return getDao().findByUsername(username);
    }

    @Override
    public SysUser checkMobileAndGetUser(String username) {
        SysUser user = this.findByUsername(username);
        if (user == null) {
            throw new BizException("该用户不存在");
        }
        String mobile = user.getMobile();
        if (Strings.isNullOrEmpty(mobile)) {
            throw new BizException("该用户没有手机号");
        }
        if (!mobilePattern.matcher(mobile).matches()) {
            throw new BizException("手机号格式不正确");
        }
        return user;
    }
}
