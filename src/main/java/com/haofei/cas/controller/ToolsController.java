package com.haofei.cas.controller;

import com.google.common.base.Strings;
import com.haofei.cas.entity.SysUser;
import com.haofei.cas.exception.BizException;
import com.haofei.cas.service.CaptchaCodeService;
import com.haofei.cas.service.SysUserService;
import com.haofei.cas.utils.Captcha;
import com.haofei.cas.utils.R;
import com.haofei.cas.utils.RSAUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * Created by mike on 2020/3/30 since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/tools")
@AllArgsConstructor
public class ToolsController {

    private SysUserService sysUserService;

    private CaptchaCodeService smsCodeService;

    @RequestMapping("/RSAPublic")
    public R rsaPublic() throws Exception {
        KeyPair keyPair = RSAUtils.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        R r = R.ok();
        r.put("modulus", publicKey.getModulus().toString(16));
        r.put("exponent", publicKey.getPublicExponent().toString(16));
        return r;
    }

    @RequestMapping("/getCaptcha")
    public void getCaptcha(String username, HttpServletResponse response) throws Exception{
        int intWidth = 190;
        int intHeight = 38;
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        //返回验证码和图片的map
        Map<String,Object> map = Captcha.getImageCode(intWidth, intHeight);
        // request.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        //request.getSession().setAttribute("codeTime",System.currentTimeMillis());
        try {
            ImageIO.write((BufferedImage) map.get("image"),"jpg", os);
        } catch (IOException ignored) {
        }   finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
        if(!Strings.isNullOrEmpty(username)) {
            try {
                SysUser sysUser = sysUserService.checkMobileAndGetUser(username);
                smsCodeService.setImageCode(sysUser.getMobile(), map.get("strEnsure").toString().toLowerCase());
            } catch (BizException e) {
                log.error("生成图形验证码异常", e);
            }
        }
    }

}
