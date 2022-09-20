package com.dwsc.constant;

import org.springframework.stereotype.Component;

/**
 * @author chennanjiang
 * @title LoginConstant
 * @date 2022/9/19 18:42
 * @description 登录常量 用户类型  数据库查询语句
 */
@Component
public class LoginConstant {
    public static final String ADMIN_TYPE="admin";
    public static final String COMMON_TYPE="common_user";
    public static final String MEMBER_TYPE="member";

}
