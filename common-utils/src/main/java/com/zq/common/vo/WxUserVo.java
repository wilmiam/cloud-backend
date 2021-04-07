package com.zq.common.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxUserVo extends PageReqVo {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 平台注册id
     */
    private Long sellerId;

    /**
     * 用户所属于的微信公众号
     */
    private String authAppId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String headimgurl;

    /**
     *  用户openId
     */
    private String openId;


    private String email;

    /**
     * 性别 1.男   2.女
     */
    private Integer sex;

    private String language;

    private String city;

    private String province;

    private String country;

    /**
     * 是否订阅
     */
    private Integer subscribe;

    /**
     * 订阅时间
     */
    private Date subscribeTime;

    private Integer groupid;

    /**
     * 备注
     */
    private String remark;

    /**
     * 访问的ip
     */
    private String accessIp;

    /**
     * token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * TOKEN 过期时间
     */
    private String tokenExpiresIn;

    /**
     * 登录时间
     */
    private Date lastLoginTime;

    /**
     *
     */
    private String unionid;

    /**
     * 积分
     */
    private Integer score;

    /**
     * 是否消息接收者 0为否，1为true
     */
    private Integer isReceiver;

    /**
     * 删除标记
     */
    private Boolean active;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 1 微信用户 2 手机用户
     */
    private Integer userType;
}
