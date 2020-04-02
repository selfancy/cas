package com.haofei.cas.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class XMessageDigest {

    public static MessageDigest getInstance(String algorithm){
        try {
         return  MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
