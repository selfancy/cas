package com.haofei.cas.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.haofei.cas.entity.SysSmsCode;
import com.haofei.cas.exception.BizException;
import com.haofei.cas.utils.SendHttpUtil;
import com.haofei.cas.utils.StringUtil;
import com.haofei.cas.utils.XDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jiangwen
 * @Date: 2020/3/11 11:41
 */
@Component
public class CaptchaCodeService {

    private static final String GEETEST_SMS_URL = "https://tectapi.geetest.com/message";
    private static final String GEETEST_SMS_APP_ID = "3c3a86a63ab13c380c5b2f6cb880fb47";
    private static final String GEETEST_SMS_KEY = "f45dbe33d0258c687b2383ca89d9f247";
    private static final String GEETEST_MODE_ID = "100439";
    private static final Integer SMS_TIME_OUT = 3;  //3分钟有效时间

    private Logger logger = LoggerFactory.getLogger(CaptchaCodeService.class);

    @Autowired
    private SysSmsCodeService sysSmsCodeService;

    public void setImageCode(String mobile, String code) {
        SysSmsCode sysSmsCode = sysSmsCodeService.getSysSmsCodeBy(mobile, SysSmsCode.OpType.Image.getValue());
        boolean update = true;
        if (sysSmsCode == null) {
            update = false;
            sysSmsCode = new SysSmsCode();
            sysSmsCode.setOpType(SysSmsCode.OpType.Image.getValue());
            sysSmsCode.setStatus(SysSmsCode.Status.Unused.getValue());
            sysSmsCode.setMobile(mobile);
        }
        sysSmsCode.setSmsCode(code);
        sysSmsCode.setStatus(SysSmsCode.Status.Unused.getValue());
        if (update) {
            sysSmsCodeService.update(sysSmsCode);
        } else {
            sysSmsCodeService.save(sysSmsCode);
        }
    }


    public void sendSmsCode(String mobile) {
        SysSmsCode sysSmsCode = sysSmsCodeService.getSysSmsCodeBy(mobile, SysSmsCode.OpType.Login.getValue());
        Boolean update = true;
        if (sysSmsCode == null) {
            update = false;
            sysSmsCode = new SysSmsCode();
            sysSmsCode.setOpType(SysSmsCode.OpType.Login.getValue());
            sysSmsCode.setStatus(SysSmsCode.Status.Unused.getValue());
            sysSmsCode.setMobile(mobile);
        } else {
            Long seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - sysSmsCode.getUpdatedTime().getTime());
            if (seconds < 55) {
                throw new BizException("一分钟只能发一次短信");
            }
        }
        sysSmsCode.setSmsCode(generateSms());
        sysSmsCode.setStatus(SysSmsCode.Status.Unused.getValue());
        if (update) {
            sysSmsCodeService.update(sysSmsCode);
        } else {
            sysSmsCodeService.save(sysSmsCode);
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "登录");
        params.put("sms", sysSmsCode.getSmsCode());
        params.put("time", SMS_TIME_OUT + "");
        sendGeetestSms(mobile, GEETEST_MODE_ID, params);
    }

    private Random random = new Random();

    private String generateSms() {
        return 100000 + random.nextInt(Integer.MAX_VALUE) % 900000 + "";
    }


    public void checkCode(String code, SysSmsCode.OpType opType, String mobile, Boolean updateSmsStatus) {
        if (StringUtil.isBlank(mobile)) {
            throw new BizException("手机号不能为空");
        }
        if (StringUtil.isBlank(code)) {
            throw new BizException(opType.getDesc() + "不能为空");
        }
        SysSmsCode sysSmsCode = sysSmsCodeService.getSysSmsCodeBy(mobile, opType.getValue());
        if (sysSmsCode == null) {
            throw new BizException(opType.getDesc() + "错误");
        }
        if (sysSmsCode.getSmsCode() == null) {
            throw new BizException(opType.getDesc() + "错误1");
        }
        if ((!sysSmsCode.getSmsCode().equalsIgnoreCase(code))) {
            throw new BizException(opType.getDesc() + "错误2");
        }

        if (sysSmsCode.isUsed()) {
            throw new BizException(opType.getDesc() + "已使用");
        }
        long minutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - sysSmsCode.getUpdatedTime().getTime());
        if (minutes >= 3) {
            throw new BizException(opType.getDesc() + "已过期");
        }
        if (updateSmsStatus) {
            sysSmsCode.setStatus(SysSmsCode.Status.Used.getValue());
            sysSmsCodeService.update(sysSmsCode);
        }
    }


    private void sendGeetestSms(String mobile, String modeId, Map<String, String> args) {
        String nonce = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        String timestamp = System.currentTimeMillis() / 1000 + "";
        Map<String, String> params = new TreeMap<>(Comparator.reverseOrder());
        params.put("gt_id", GEETEST_SMS_APP_ID);
        params.put("nonce", nonce);
        params.put("timestamp", timestamp);

        List<String> values = Lists.newArrayList(params.values());
        Collections.sort(values);
        String content = Joiner.on("").join(values);
        logger.debug("geetest send sms content:{}", content);
        String signature = XDigest.hMacsSha256(content, GEETEST_SMS_KEY);
        params.put("signature", signature);
        String Authorization = Joiner.on(",").withKeyValueSeparator("=").join(new TreeMap<>(params));

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", Authorization);
        logger.debug("geetest send sms Authorization:{}", Authorization);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", mobile);
        jsonObject.put("modeId", modeId);
        JSONObject arguments = new JSONObject();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            arguments.put(entry.getKey(), entry.getValue());
        }
        jsonObject.put("arguments", arguments);
        logger.debug("geetest send sms jsonString:{}", jsonObject.toJSONString());
        try {
            String s = SendHttpUtil.sendPost(jsonObject.toJSONString(), GEETEST_SMS_URL, headers);
            logger.info("mobile:{},arguments:{},responseData:{}", mobile, arguments, s);
        } catch (Exception e) {
            logger.error("SmsService send Sms occurred an exception", e);
            throw new BizException("发送短信失败");
        }
    }
}
