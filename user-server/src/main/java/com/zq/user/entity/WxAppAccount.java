package com.zq.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 微信APP账号(WxAppAccount)实体类
 *
 * @author makejava
 * @since 2021-10-08 16:46:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "t_wx_app_account")
public class WxAppAccount {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 微信APP_ID
     */
    @ApiModelProperty("微信APP_ID")
    private String appId;

    /**
     * 微信APP秘钥
     */
    @ApiModelProperty("微信APP秘钥")
    private String appSecret;

    /**
     * APP名称
     */
    @ApiModelProperty("APP名称")
    private String appName;

    /**
     * 状态：1-正常
     */
    @ApiModelProperty("状态：1-正常")
    private Integer state;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
