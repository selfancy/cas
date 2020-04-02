package com.haofei.cas.exception;

import org.apereo.cas.authentication.RootCasException;

/**
 * 业务异常
 *
 * Created by mike on 2020/3/31 since 1.0
 */
public class BizException extends RootCasException {

    public BizException(String msg) {
        super("BIZ_INVALID_OPERATION", msg);
    }

    public BizException(Throwable throwable) {
        super("BIZ_INVALID_OPERATION", throwable);
    }
}
