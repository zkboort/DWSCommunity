package com.dwsc.configs.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @author chennanjiang
 * @title JetcacheConfig
 * @date 2022/9/20 11:45
 * @description TODO
 */
@Configuration
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.dwsc.service.serviceImpl")
public class JetcacheConfig {

}
