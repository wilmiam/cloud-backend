package com.zq.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wilmiam
 * @since 2021-08-28 11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxLoginVo {

    @ApiModelProperty("登入的token")
    private String token;

    @ApiModelProperty("小程序appId")
    private String appId;

    @ApiModelProperty("登录凭证 code")
    private String code;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("unionId")
    private String unionId;

    @ApiModelProperty("加密数据")
    private String encryptData;

    @ApiModelProperty("解量")
    private String ivData;

    @ApiModelProperty("登录后获取的 key")
    private String sessionKey;

    @ApiModelProperty("微信用户头像")
    private String avatar;

    @ApiModelProperty("微信用户昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("性别")
    private String sex;

}
