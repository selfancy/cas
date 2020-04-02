package com.haofei.cas.sms;

import com.haofei.cas.utils.StringUtil;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.web.support.WebUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
public class SmsCodeLoginAction extends AbstractAction {

    private static final String PHONE_CODE = "phoneError";
    private static final String SMS_CODE = "smsCodeError";

    @Override
    protected Event doExecute(RequestContext requestContext) throws Exception {
        Credential credential = WebUtils.getCredential(requestContext);
        if (credential instanceof SmsCodeCredential) {
            SmsCodeCredential codeCredential = (SmsCodeCredential) credential;
            String phone = codeCredential.getPhone();
            String smsCode = codeCredential.getSmsCode();

            //字段为空提交时
            //将登录类型一个全局变量里
            //电话号码为空校验
            if (StringUtil.isBlank(phone)) {
                return getError(requestContext, PHONE_CODE);
            }
            //电话号码为空校验
            if (StringUtil.isBlank(smsCode)) {
                return getError(requestContext, SMS_CODE);
            }
        }
        return null;
    }

    /**
     * 跳转到错误页
     *
     * @param requestContext
     * @return
     */
    private Event getError(final RequestContext requestContext, String CODE) {
        final MessageContext messageContext = requestContext.getMessageContext();
        messageContext.addMessage(new MessageBuilder().error().code(CODE).build());
        return getEventFactorySupport().event(this, CODE);
    }
}
