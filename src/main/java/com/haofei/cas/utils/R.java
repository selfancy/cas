package com.haofei.cas.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author chenshun
 * @date 2016年10月27日 下午9:59:27
 * sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final Integer OK = 0;

    public static final Integer ERROR = 500;

    public R() {
        put("code", OK);
    }

    public static R error() {
        return error(ERROR, "操作失败，请联系管理员");
    }

    public static R error(String msg) {
        return error(ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public String getCode() {
        Object code = this.get("code");
        if (code == null) {
            return null;
        }
        return code.toString();
    }

    public String getMsg() {
        return (String) this.get("msg");
    }
}
