package com.haofei.cas.utils;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.util.Locale;

/**
 * 全局工具类
 *
 * Created by mike on 2020/3/31 since 1.0
 */
public class ApplicationUtil {

    private ApplicationUtil() {
    }

    private static BeanFactory beanFactory;

    public static BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 设置 Bean 工厂
     * @param beanFactory
     */
    public static void saveBeanFactory(BeanFactory beanFactory) {
        ApplicationUtil.beanFactory = beanFactory;
    }

    /**
     * 获取 Bean
     *
     * @param name
     */
    public static Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    /**
     * 获取 Bean
     *
     * @param clazz
     * @param <T>
     */
    public static <T> T getBean(Class<T> clazz) {
        return getBeanFactory().getBean(clazz);
    }

    /**
     * 包含 Bean
     *
     * @param name
     */
    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    /**
     * 获取环境参数
     */
    public static Environment getEnvironment() {
        return getBeanFactory().getBean(Environment.class);
    }

    /**
     * 查找 Bean上的注解
     *
     * @param beanName
     * @param annotationType
     * @param <A>
     */
    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return ((ListableBeanFactory) getBeanFactory()).findAnnotationOnBean(beanName, annotationType);
    }

    /**
     * 获取 i18n 转换资源信息
     * @param code  定义资源 code
     * @param args  占位参数
     * @param defaultMessage    默认信息(资源未找到时使用)
     * @param locale    本地语言
     * @return
     */
    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return getBeanFactory().getBean(MessageSource.class).getMessage(code, args, defaultMessage, locale);
    }

    /**
     * 获取 i18n 转换资源信息
     * @param code  定义资源 code
     * @param args  占位参数
     * @param locale    本地语言
     * @return
     */
    public static String getMessage(String code, Object[] args, Locale locale) {
        return getBeanFactory().getBean(MessageSource.class).getMessage(code, args, locale);
    }

    /**
     * 获取 i18n 转换资源信息
     * @param resolvable  资源解析器
     * @param locale    本地语言
     * @return
     */
    public static String getMessage(MessageSourceResolvable resolvable, Locale locale) {
        return getBeanFactory().getBean(MessageSource.class).getMessage(resolvable, locale);
    }
}
