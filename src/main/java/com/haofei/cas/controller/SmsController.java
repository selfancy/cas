package com.haofei.cas.controller;

import com.haofei.cas.entity.SysSmsCode;
import com.haofei.cas.entity.SysUser;
import com.haofei.cas.service.CaptchaCodeService;
import com.haofei.cas.service.SysUserService;
import com.haofei.cas.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
@RestController
@RequestMapping("/biz/sms")
@AllArgsConstructor
public class SmsController {

    private final SysUserService sysUserService;

    private final CaptchaCodeService smsCodeService;

    @RequestMapping(value = "/sendSmsCode")
    public R sendSmsCode(String username, String captcha) {
        SysUser user = sysUserService.checkMobileAndGetUser(username);
        String mobile = user.getMobile();
        smsCodeService.checkCode(captcha, SysSmsCode.OpType.Image, mobile, false);
        smsCodeService.sendSmsCode(mobile);
        smsCodeService.checkCode(captcha, SysSmsCode.OpType.Image, mobile, true);
        return R.ok().put("mobile", mobile.substring(mobile.length() - 4));
    }
}
