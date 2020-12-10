package com.gerry.pang.common.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前用户
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentUserDTO implements Serializable {
    private static final long serialVersionUID = 5118779706960433394L;

    /**
     * 用户id
     */
    private String id;

    /**
     * 域账号
     */
    private String userAccount;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户角色集合
     */
    private Set<String> roles;

    /**
     * 用户权限集合
     */
    private Set<String> permissions;

    /**
     * 用户头像
     */
    private String headPortraits;

}
