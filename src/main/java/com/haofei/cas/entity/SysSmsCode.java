package com.haofei.cas.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信
 *
 * Created by mike on 2020/3/31 since 1.0
 */
@Data
@ToString
public class SysSmsCode implements Serializable {

    /**
     * 主键id
     */
    private Long pid;
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 短信验证码
     */
    private String smsCode;
    /**
     * 1.登录
     */
    private Integer opType;
    /**
     * 1未使用，2已使用，3已失效
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;

    public boolean isUnused(){
        return Status.Unused.getValue() == this.status;
    }

    public boolean isUsed(){
        return Status.Used.getValue() == this.status;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public enum OpType{

        Login(1,"登录短信验证码"),
        Image(2,"图形验证码");

        private final int value;
        private final String desc;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public enum Status{
        Unused(1), Used(2);

        private int value;
    }
}
