package com.dwsc.configs.mybatisPlus;

import com.baomidou.mybatisplus.annotation.DbType;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chennanjiang
 * @title MybatisPlusConfig
 * @date 2022/9/20 11:48
 * @description TODO
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * @description:  分页插件
     * @date 2022/9/20 11:50
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
         PaginationInnerInterceptor paginationInnerInterceptor=new PaginationInnerInterceptor();
         paginationInnerInterceptor.setDbType(DbType.MYSQL);
         return paginationInnerInterceptor;
    }

    /**
     * @description: 删除时乐观锁    @Version
     * @date 2022/9/20 12:01
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor(){
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor=new OptimisticLockerInnerInterceptor();
        return optimisticLockerInnerInterceptor;
    }

    /**
     * @description: 主键生成器  ID_WORK :数字  ID_WORK_STR：字符串
     * @date 2022/9/20 12:05
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    public IKeyGenerator iKeyGenerator(){
        H2KeyGenerator h2KeyGenerator=new H2KeyGenerator();
        return h2KeyGenerator;
    }
}
