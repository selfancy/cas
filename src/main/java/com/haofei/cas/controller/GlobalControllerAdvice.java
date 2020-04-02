//package com.haofei.cas.controller;
//
//import com.haofei.cas.utils.R;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * Created by mike on 2020/3/31 since 1.0
// */
//@Slf4j
//@RestControllerAdvice
//public class GlobalControllerAdvice {
//
//    @ExceptionHandler
//    public R handleException(Exception origin) {
//        log.error("cas系统异常", origin);
//        Throwable cause = ExceptionUtils.getRootCause(origin);
//        if (cause != null) {
//            return R.error(cause.getMessage());
//        }
//        return R.error(origin.getMessage());
//    }
//}
