package com.zq.user.vo;

import com.zq.common.vo.PageReqVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户表(TAppUser)实体类
 *
 * @author wilmiam
 * @since 2021-07-05 10:21:25
 */
@Data
public class FindAppUserVo extends PageReqVo {

    /**
     * id
     */
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
     * 性别
     */
    @ApiModelProperty("性别")
    private String gender;

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
     * 上级
     */
    @ApiModelProperty("上级")
    private Long pid;

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

}
