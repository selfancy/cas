package com.haofei.cas.config;

import lombok.AllArgsConstructor;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
@Configuration
@AllArgsConstructor
public class CustomAuthenticationEventExecutionPlanConfigurer implements AuthenticationEventExecutionPlanConfigurer {

    @Qualifier("smsCodeAuthenticationHandler")
    private AuthenticationHandler smsCodeAuthenticationHandler;

    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(smsCodeAuthenticationHandler);
    }
}
