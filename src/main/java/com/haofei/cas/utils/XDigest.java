package com.haofei.cas.utils;

import com.haofei.cas.exception.BizException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Formatter;


/**
 * @author lizhi
 */
public class XDigest {

    public static String encryptMd5(String data, int times) {
        String result = data;
        for (int i = 0; i < times; i++) {
            result = encryptMd5(result);
        }
        return result;
    }

    /**
     * 使用md5算法加密字符串
     *
     * @param data
     * @return
     */
    public static String encryptMd5(String data) {
        MessageDigest digest = null;
        digest = XMessageDigest.getInstance("MD5");
        digest.update(StringUtil.getUtf8Bytes(data));
        byte[] byteResult = digest.digest();
        return Hex.encodeHexString(byteResult);
    }

    public static String hMacsSha256(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = convertToHex(bytes);
        } catch (Exception e) {
            throw new BizException(e);
        }
        return hash;
    }

    private static String convertToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String hexString = formatter.toString();
        formatter.close();
        return hexString;
    }
}
