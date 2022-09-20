package com.dwsc.config;

import com.dwsc.service.serviceImpl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * @author chennanjiang
 * @title AuthorizationConfig
 * @date 2022/9/19 16:15
 * @description 用户请求授权配置
 */
@EnableAuthorizationServer //开启授权服务功能
@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;   //加密解密器
    @Autowired
    private AuthenticationManager authenticationManager; //验证管理器
    @Autowired
    private MyUserDetailsService userDetailsService;  //自定义用户数据从数据库中获取

    /**
     * @description:  添加第三方客户端
     * @date 2022/9/19 16:19
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("DWS-pai") //第三方客户端名称
                .secret(passwordEncoder.encode("DWS-secret")) //加密解密秘钥
                .scopes("all") //授权范围
                .authorizedGrantTypes("password","refresh_token") //密码授权，通过refresh_token来为过期的token获取新的token数据
                .accessTokenValiditySeconds(7*24*3600) //token有效时间
                .refreshTokenValiditySeconds(14*24*3600); //refresh_token有效期
        super.configure(clients);
    }

    /**
     * @description:  验证管理器配置 UserdetailService
     * @date 2022/9/19 16:29
     * @author chennanjiang
     * @params  * @param null
     * @return {@link null}
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(jwtTokenStore()) //定义token存放，使用JWT
                .tokenEnhancer(jwtAccessTokenConverter());  //使用转换器，数据转为json存储
    }

    public JwtTokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter tokenConverter= new JwtAccessTokenConverter();
        //本地生成私钥后放入资源目录下 加载私钥
        ClassPathResource classPathResource=new ClassPathResource("dwscommunity.jks");
        //openssl证书生成秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(classPathResource,"dwscommunity".toCharArray());
        tokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("dwscommunity","dwscommunity".toCharArray()));
        return  tokenConverter;
    }
}
