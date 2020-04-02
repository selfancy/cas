package com.haofei.cas.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 图形验证码
 */
public class Captcha {

    private static final String CAPTCHA_CHARS = "0123456789ABCDEFGHIJKMNOPQRSTUVWXYZ";

    public static Map<String, Object> getImageCode(int width, int height) {
        Map<String, Object> returnMap = new HashMap<>();
        if (width <= 0) {
            width = 60;
        }
        if (height <= 0) {
            height = 20;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        //生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        //设定字体
        g.setFont(new Font("Arial", Font.ITALIC, 25));
        // 随机产生168条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 168; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        //取随机产生的码
        StringBuilder strEnsure = new StringBuilder();
        //4代表4位验证码,如果要生成更多位的认证码,则加大数值
        int indexwidth = width / 5;
        for (int i = 0; i < 4; ++i) {
            strEnsure.append(CAPTCHA_CHARS.charAt((int) (CAPTCHA_CHARS.length() * Math.random())));
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 直接生成
            String str = strEnsure.substring(i, i + 1);
            // 设置随便码在背景图图片上的位置
            g.drawString(str, indexwidth * i + indexwidth - 5, 20 + random.nextInt((height - 20) / 2));
        }
        // 释放图形上下文
        g.dispose();
        returnMap.put("image", image);
        returnMap.put("strEnsure", strEnsure.toString());
        return returnMap;
    }

    //给定范围获得随机颜色
    static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}