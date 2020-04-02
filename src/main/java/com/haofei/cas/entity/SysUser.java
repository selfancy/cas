package com.haofei.cas.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录用户信息
 *
 * Created by mike on 2020/3/31 since 1.0
 */
@Data
public class SysUser implements Serializable {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 是否过期
     */
    private boolean expired;
    /**
     * 是否禁用
     */
    private boolean disabled;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;
}
