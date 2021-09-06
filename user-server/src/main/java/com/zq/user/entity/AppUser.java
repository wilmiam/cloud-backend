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
 * 用户表(TAppUser)实体类
 *
 * @author wilmiam
 * @since 2021-07-05 10:21:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "t_app_user")
public class AppUser {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String account;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    private String password;

    /**
     * 交易密码
     */
    @ApiModelProperty("交易密码")
    private String dealpwd;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private Integer userType;

    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String realname;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Integer age;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 邀请码
     */
    @ApiModelProperty("邀请码")
    private String inviteCode;

    /**
     * 邀请二维码连接
     */
    @ApiModelProperty("邀请二维码连接")
    private String inviteLink;

    /**
     * 上级
     */
    @ApiModelProperty("上级")
    private Long pid;

    /**
     * 所有上级ID线路，上级在左
     */
    @ApiModelProperty("所有上级ID线路，上级在左")
    private String pids;

    /**
     * 用户等级
     */
    @ApiModelProperty("用户等级")
    private Integer level;

    /**
     * 状态：0未激活 1正常 2暂停使用 3永久停号
     */
    @ApiModelProperty("状态：0未激活 1正常 2暂停使用 3永久停号")
    private Integer status;

    /**
     * 开放平台获取的unionid,解决这个同一个企业的不同APP和不同公众号之间的帐号共通
     */
    @ApiModelProperty("开放平台获取的unionid,解决这个同一个企业的不同APP和不同公众号之间的帐号共通")
    private String unionId;

    /**
     * 最后登录访问IP
     */
    @ApiModelProperty("最后登录访问IP")
    private String accessIp;

    /**
     * 区域码
     */
    @ApiModelProperty("区域码")
    private String areaCode;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;

}
