package com.haofei.cas.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * 字符串工具类
 *
 * Created by mike on 2020/3/31 since 1.0
 */
public final class StringUtil extends StringUtils {

    private StringUtil() {}

    public static byte[] getUtf8Bytes(String s) {
        if (s != null) {
            return s.getBytes(StandardCharsets.UTF_8);
        }
        return new byte[0];
    }
}
