package com.haofei.cas.sms;

import com.haofei.cas.entity.SysSmsCode;
import com.haofei.cas.entity.SysUser;
import com.haofei.cas.exception.BizException;
import com.haofei.cas.service.CaptchaCodeService;
import com.haofei.cas.service.SysUserService;
import com.haofei.cas.utils.CasAuthenticationUtil;
import com.haofei.cas.utils.StringUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.AccountPasswordMustChangeException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ThemeResolver;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * 短信验证码登录
 * Created by mike on 2020/3/30 since 1.0
 */
@Slf4j
public class SmsCodePrePostAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    @Setter private AuthenticationHandler baseAuthenticationHandler;

    @Setter private ThemeResolver themeResolver;

    private static final String GAME_MANAGER_PRO_THEME_NAME = "game_manager_pro";

    @Setter private SysUserService sysUserService;

    @Setter private CaptchaCodeService captchaCodeService;

    public SmsCodePrePostAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String dynamicThemeName = themeResolver.resolveThemeName(request);
        if (Objects.equals(dynamicThemeName, GAME_MANAGER_PRO_THEME_NAME)) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            String username = usernamePasswordCredential.getUsername();
            String smsCode = usernamePasswordCredential.getPassword();
            SysUser sysUser = sysUserService.findByUsername(username);
            if (sysUser != null) {
                checkUser(sysUser, smsCode);
                final Principal principal = this.principalFactory.createPrincipal(username, new LinkedHashMap<>(0));
                AuthenticationHandlerExecutionResult result = createHandlerResult(credential, principal, new ArrayList<>(0));
                CasAuthenticationUtil.setAuthentication(result);
                return result;
            }
            throw new AccountNotFoundException(username + " not found");
        }
        AuthenticationHandlerExecutionResult result = baseAuthenticationHandler.authenticate(credential);
        CasAuthenticationUtil.setAuthentication(result);
        return result;
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof UsernamePasswordCredential;
    }

    private void checkUser(SysUser sysUser, String smsCode) throws GeneralSecurityException {
        if (StringUtil.isBlank(sysUser.getMobile())) {
            throw new FailedLoginException("Phone number does not exist.");
        }
        try {
            captchaCodeService.checkCode(smsCode,SysSmsCode.OpType.Login, sysUser.getMobile(),true);
        } catch (BizException e) {
            throw new FailedLoginException(e.getMessage());
        }
        if (sysUser.isDisabled()) {
            throw new AccountDisabledException("Account has been disabled");
        }
        if (sysUser.isExpired()) {
            throw new AccountPasswordMustChangeException("Password has expired");
        }
    }
}
