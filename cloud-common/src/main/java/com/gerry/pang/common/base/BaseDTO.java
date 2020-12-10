package com.gerry.pang.common.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO基类
 *
 * @className BaseDTO
 * @vsersion 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDTO implements Serializable {
    private static final long serialVersionUID = 5118779706960433394L;

    /**
     * id
     */
    private String id;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime; // 创建时间

    /**
     * 创建人
     */
    private String creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 最后修改时间
     */
    private Date updateTime;

    /**
     * 最后修改人
     */
    private String updatorId;

    /**
     * 最后修改人名称
     */
    private String updatorName;

    /**
     * 分页：当前页
     */
    private Integer current = 1;

    /**
     * 每页记录数
     */
    private Integer rowCount = 10;

    /**
     * 删除标识
     */
    private Boolean deleted = false;

    /**
     * 当前登录用户
     */
    private CurrentUserDTO currentUser;

}