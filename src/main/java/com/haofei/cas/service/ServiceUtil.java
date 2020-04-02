package com.haofei.cas.service;

import com.haofei.cas.dao.BaseDao;
import com.haofei.cas.utils.ApplicationUtil;
import com.haofei.cas.utils.ClassUtil;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Service工具类
 *
 * Created by mike on 2020/3/31 since 1.0
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
class ServiceUtil {

    private static volatile Map<Class<? extends BaseService>, BaseDao> serviceDaoMap = new ConcurrentHashMap<>(16);

    /**
     * 通过service接口泛型Dao类型获取Dao实例对象
     *
     * @param baseService service接口
     * @param <T>         实体类
     * @param <DAO>       Dao接口
     * @return Dao接口对象
     */
    static <T, DAO extends BaseDao<T>> DAO getDao(BaseService<T, DAO> baseService) {
        final Class<? extends BaseService> serviceClass = baseService.getClass();
        DAO dao = (DAO) serviceDaoMap.get(serviceClass);
        if (dao != null) {
            return dao;
        }
        final Type[] genericInterfaces = serviceClass.getGenericInterfaces();
        dao = Stream.of(genericInterfaces)
                .filter(interfaceClass -> BaseService.class.isAssignableFrom((Class) interfaceClass))
                .findFirst()
                .map(interfaceClass -> {
                    final Class<?> baseServiceClass = (Class<?>) interfaceClass;
                    final Class<? extends DAO> daoClass = ClassUtil.getInterfaceGenericClass(baseServiceClass, 1);
                    return ApplicationUtil.getBean(daoClass);
                }).orElse(null);
        if (dao != null) {
            serviceDaoMap.put(serviceClass, dao);
            return dao;
        }
        throw new RuntimeException("无法找到Dao接口class");
    }
}
