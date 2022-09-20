package com.dwsc.service.serviceImpl;

import com.dwsc.constant.LoginConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author chennanjiang
 * @title myUserDetailsService
 * @date 2022/9/19 18:51
 * @description 自定义UserDetailsService 返回UserDetails对象
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //确定登录类型
        String loginType=requestAttributes.getRequest().getParameter("login_type");
        if(StringUtils.isEmpty(loginType)){
            throw new AuthenticationServiceException("登录类型不能为空");
        }
        UserDetails userDetails=null;
        try {
            switch (loginType){
                case LoginConstant.ADMIN_TYPE:
                    userDetails= loadSysUserByusername(username);
                    break;
                case LoginConstant.COMMON_TYPE:
                    userDetails= loadCommonUserByUsername(username);
                    break;
                case LoginConstant.MEMBER_TYPE:
                    userDetails= loadMemberUserByUsername(username);
                    break;
                default:
                    throw new AuthenticationServiceException("暂不支持此类型用户登录："+loginType);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UsernameNotFoundException("用户名"+username+"不存在");
        }
        return userDetails;
    }


    private UserDetails loadMemberUserByUsername(String username) {
        //根据用户名查询数据库
        //使用security的User的创建方法创建userDetails对象
        UserDetails user= User.builder()
                .username("")
                .password("")
                .roles("")
                .build();
        return user;
    }

    private UserDetails loadCommonUserByUsername(String username) {
        return null;
    }

    private UserDetails loadSysUserByusername(String username) {
        return null;
    }
}
