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
 * 微信用户表(WxUser)实体类
 * 基本信息字段, 不对这张表做字段加减, 如有其它字段在相应模块添加表做关联
 *
 * @author makejava
 * @since 2021-10-08 16:33:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "WX_USER")
public class WxUser {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 微信昵称
     */
    @ApiModelProperty("微信昵称")
    private String wxName;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String passwd;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 身份证号码
     */
    @ApiModelProperty("身份证号码")
    private String idCard;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Integer age;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 微信获取的地址
     */
    @ApiModelProperty("微信获取的地址")
    private String address;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer state;

    /**
     * 微信APPID
     */
    @ApiModelProperty("微信APPID")
    private String appId;

    /**
     * 微信openId
     */
    @ApiModelProperty("微信openId")
    private String openId;

    /**
     * 微信开放平台获取的unionId
     */
    @ApiModelProperty("微信开放平台获取的unionId")
    private String unionId;

    /**
     * 最后访问IP
     */
    @ApiModelProperty("最后访问IP")
    private String accessIp;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;

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
