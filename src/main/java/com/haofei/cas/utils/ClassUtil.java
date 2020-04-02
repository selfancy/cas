package com.haofei.cas.utils;

import com.google.gson.internal.$Gson$Types;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * class工具类
 *
 * Created by mike on 2020/3/31 since 1.0
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class ClassUtil extends ClassUtils {

    private ClassUtil() {
    }

    /**
     * 获取类的泛型参数类型(第一个泛型)
     *
     * @param targetClass    目标子类 class
     * @param <T>
     * @return
     */
    public static <T> Class<T> getGenericClass(Class<?> targetClass) {
        return getGenericClass(targetClass, 0);
    }

    /**
     * 获取类的泛型参数类型
     *
     * @param targetSubClass    目标子类 class
     * @param genericIndex 类泛型参数下标位置
     * @param <T>
     * @return
     */
    public static <T> Class<T> getGenericClass(Class<?> targetSubClass, int genericIndex) {
        Type type = targetSubClass.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments((ParameterizedType) type);
            Type genericType = CollectionUtils.get(typeArguments.values(), genericIndex);
            if (genericType instanceof Class) {
                return (Class<T>) genericType;
            }
        } else if (type instanceof Class) {
            return getGenericClass(targetSubClass.getSuperclass(), genericIndex);
        }
        return (Class<T>) targetSubClass;
    }

    /**
     * 获取实现类第一个接口的泛型参数类型
     *
     * @param clazz        当前类
     * @param genericIndex 泛型参数下标位置
     * @param <T>
     * @return
     */
    public static <T> Class<T> getInterfaceGenericClass(Class<?> clazz, int genericIndex) {
        return getInterfaceGenericClass(clazz, 0, genericIndex);
    }

    /**
     * 获取实现类接口的泛型参数类型
     *
     * @param clazz        当前类
     * @param genericIndex 泛型参数下标位置
     * @param <T>
     * @return
     */
    public static <T> Class<T> getInterfaceGenericClass(Class<?> clazz, int interfaceIndex, int genericIndex) {
        Type[] types = clazz.getGenericInterfaces();
        Type type = types[interfaceIndex];
        if (type instanceof ParameterizedType) {
            Type[] args = ((ParameterizedType) type).getActualTypeArguments();
            return (Class<T>) $Gson$Types.getRawType($Gson$Types.canonicalize(args[genericIndex]));
        }
        return (Class<T>) clazz;
    }

    /**
     * 获取在指定包下某个class的所有非抽象子类
     *
     * @param parentClass
     *            父类
     * @param packagePath
     *            指定包，格式如"com/iteye/strongzhu"
     * @return 该父类对应的所有子类列表
     */
    public static <E> List<Class<E>> getSubClasses(final Class<E> parentClass, final String packagePath) {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                false);
        provider.addIncludeFilter(new AssignableTypeFilter(parentClass));
        final Set<BeanDefinition> components = provider
                .findCandidateComponents(packagePath);
        final List<Class<E>> subClasses = new ArrayList<>();
        for (final BeanDefinition component : components) {
            Class<E> cls = null;
            try {
                cls = (Class<E>) getClass(component.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (Modifier.isAbstract(cls.getModifiers())) {
                continue;
            }
            subClasses.add(cls);
        }
        return subClasses;
    }

    /**
     * 获取基本类型
     *
     * @param packageType
     * @return
     */
    public static Class<?> getBasicType(Class<?> packageType) {
        if (packageType == null) return null;
        if (packageType.equals(Integer.class)) return int.class;
        if (packageType.equals(Long.class)) return long.class;
        if (packageType.equals(Double.class)) return double.class;
        if (packageType.equals(Float.class)) return float.class;
        if (packageType.equals(Short.class)) return short.class;
        if (packageType.equals(Boolean.class)) return boolean.class;
        if (packageType.equals(Byte.class)) return byte.class;
        if (packageType.equals(CharSequence.class)) return char.class;
        else return packageType;
    }

    /**
     * 获取包装类型
     *
     * @param basicType
     * @return
     */
    public static Class<?> getPackageType(Class<?> basicType) {
        if (basicType == null) return null;
        if (basicType.equals(int.class)) return Integer.class;
        if (basicType.equals(long.class)) return Long.class;
        if (basicType.equals(double.class)) return Double.class;
        if (basicType.equals(float.class)) return Float.class;
        if (basicType.equals(short.class)) return Short.class;
        if (basicType.equals(boolean.class)) return Boolean.class;
        if (basicType.equals(byte.class)) return Byte.class;
        if (basicType.equals(char.class)) return CharSequence.class;
        else return basicType;
    }

    /**
     * 判断类型是否为数值类型
     *
     * @param clazz
     * @return
     */
    public static boolean isNumber(Class<?> clazz) {
        return clazz != null &&
                (clazz.equals(int.class) || clazz.equals(long.class) || clazz.equals(double.class) ||
                        clazz.equals(float.class) || clazz.equals(short.class) || clazz.equals(byte.class) ||
                        clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Double.class) ||
                        clazz.equals(Float.class) || clazz.equals(Short.class) || clazz.equals(Byte.class));
    }
}
