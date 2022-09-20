package com.dwsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author chennanjiang
 * @title webSecurityConfig
 * @date 2022/9/19 16:34
 * @description TODO
 */
@Configuration
public class webSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * @description: http请求安全配置
     * @date 2022/9/19 16:44
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭跨域
        http.csrf().disable();
        //指定任何请求都需要授权
        http.authorizeHttpRequests().anyRequest().authenticated();
    }

    /**
     * @description: 注入授权管理器
     * @date 2022/9/19 16:39
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    /**
     * @description: 密码加密器
     * @date 2022/9/19 16:42
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
