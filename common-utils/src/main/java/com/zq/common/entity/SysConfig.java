package com.zq.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统配置表(SysConfig)实体类
 *
 * @author wilmiam
 * @since 2021-04-07 15:10:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sys_config")
public class SysConfig implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 键
     */
    @ApiModelProperty("键")
    private String code;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 排序号
     */
    @ApiModelProperty("排序号")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private Long createId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateId;

}