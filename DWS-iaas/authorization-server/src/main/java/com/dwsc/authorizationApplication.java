package com.dwsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author chennanjiang
 * @title authorizationApplication
 * @date 2022/9/19 16:13
 * @description TODO
 */
@EnableResourceServer  //改服务作为资源服务
@SpringBootApplication
public class authorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(authorizationApplication.class,args);
    }
}
