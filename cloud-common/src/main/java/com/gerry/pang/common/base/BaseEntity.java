package com.gerry.pang.common.base;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 实体基类
 *
 * @className BaseEntity
 */
@Data
public abstract class BaseEntity implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 逻辑删除标识
     */
    private Boolean isDeleted = false;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 更新人ID
     */
    private String updatorId;

    /**
     * 更新人姓名
     */
    private String updatorName;

    /**
     * 版本号
     */
    private Integer version;

}
