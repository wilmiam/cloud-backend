package com.zq.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(TMember)实体类
 *
 * @author wilmiam
 * @since 2020-10-20 12:05:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginVo {

    private Long userId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("验证码")
    private String verifyCode;

    private String passwd;

    @ApiModelProperty("登录类型")
    private String loginType;

}
