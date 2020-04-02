package com.haofei.cas.controller;

import com.haofei.cas.service.SysUserService;
import com.haofei.cas.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
@RestController
@RequestMapping("/biz/user")
@AllArgsConstructor
public class UserController {

    private SysUserService sysUserService;

    @RequestMapping("/checkUserName")
    @ResponseBody
    public R checkUserName(String username) {
        sysUserService.checkMobileAndGetUser(username);
        return R.ok();
    }
}
