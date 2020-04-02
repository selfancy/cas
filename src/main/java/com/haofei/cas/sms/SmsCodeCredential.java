package com.haofei.cas.sms;

import org.apereo.cas.authentication.UsernamePasswordCredential;

import javax.validation.constraints.Size;

/**
 * 短信验证码凭据
 *
 * Created by mike on 2020/3/30 since 1.0
 */
public class SmsCodeCredential extends UsernamePasswordCredential {
    /**
     * 手机号码
     */
    @Size(min = 11, max = 11, message = "require phone")
    private String phone;
    /**
     * 短信验证码
     */
    @Size(min = 11, max = 11, message = "require smsCode")
    private String smsCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
