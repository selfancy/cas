package com.haofei.cas.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis 配置
 *
 * Created by mike on 2020/3/31 since 1.0
 */
@Configuration
@MapperScan("com.haofei.cas.dao")
public class MybatisConfig {
}