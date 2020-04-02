package com.haofei.cas.sms;

import com.haofei.cas.service.CaptchaCodeService;
import com.haofei.cas.service.SysSmsCodeService;
import com.haofei.cas.service.SysUserService;
import lombok.AllArgsConstructor;
import org.apereo.cas.adaptors.jdbc.QueryDatabaseAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ThemeResolver;

import java.util.Collection;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
@Configuration
@EnableConfigurationProperties(CasConfigurationProperties.class)
@AutoConfigureAfter(name = "CasJdbcAuthenticationConfiguration")
@AllArgsConstructor
public class SmsCodeConfiguration {

    @Bean
    public AuthenticationHandler smsCodeAuthenticationHandler(
            ServicesManager servicesManager, @Qualifier("jdbcPrincipalFactory") PrincipalFactory principalFactory,
            @Qualifier("jdbcAuthenticationHandlers") Collection<AuthenticationHandler> jdbcAuthenticationHandlers,
            ThemeResolver themeResolver, SysUserService sysUserService, CaptchaCodeService captchaCodeService) {
        SmsCodePrePostAuthenticationHandler authenticationHandler = new SmsCodePrePostAuthenticationHandler(SmsCodePrePostAuthenticationHandler.class.getName(),
                servicesManager, principalFactory, 1);
        jdbcAuthenticationHandlers.stream().filter(handler -> QueryDatabaseAuthenticationHandler.class.getSimpleName().equals(handler.getName())).findFirst()
                .ifPresent(authenticationHandler::setBaseAuthenticationHandler);
        authenticationHandler.setThemeResolver(themeResolver);
        authenticationHandler.setSysUserService(sysUserService);
        authenticationHandler.setCaptchaCodeService(captchaCodeService);
        jdbcAuthenticationHandlers.removeIf(handler -> QueryDatabaseAuthenticationHandler.class.getSimpleName().equals(handler.getName()));
        jdbcAuthenticationHandlers.add(authenticationHandler);
        return authenticationHandler;
    }

    @Bean
    public AuthenticationEventExecutionPlanConfigurer smsCodeAuthenticationEventExecutionPlanConfigurer(
            @Qualifier("smsCodeAuthenticationHandler") AuthenticationHandler authenticationHandler) {
        return plan -> {
            plan.registerAuthenticationHandler(authenticationHandler);
        };
    }
}
