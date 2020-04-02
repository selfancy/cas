package com.haofei.cas.utils;

import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.authentication.AuthenticationBuilder;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.DefaultAuthenticationBuilder;

import java.time.ZonedDateTime;

/**
 * Created by mike on 2020/3/31 since 1.0
 */
public class CasAuthenticationUtil {

    private CasAuthenticationUtil() {}

    private static Authentication authentication;

    public static Authentication getAuthentication() {
        return authentication;
    }

    public static void setAuthentication(AuthenticationHandlerExecutionResult executionResult) {
        AuthenticationBuilder builder = DefaultAuthenticationBuilder.newInstance();
        builder.addCredential(executionResult.getCredentialMetaData());
        builder.setPrincipal(executionResult.getPrincipal());
        builder.setAuthenticationDate(ZonedDateTime.now());
        authentication = builder.build();
    }
}
