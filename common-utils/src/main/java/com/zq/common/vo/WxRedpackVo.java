package com.zq.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxRedpackVo extends PageReqVo {

    /**
     * 红包发放表
     */

    /**
     * 发放红包记录id
     */
    @ApiModelProperty("发放红包记录id")
    private Integer id;

    /**
     * 用户openId
     */
    @ApiModelProperty("用户openId")
    private String openId;

    /**
     * 发送金额
     */
    @ApiModelProperty("发送金额")
    private BigDecimal amount;

    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    private String createTime;

    /**
     * 发送结果：1 成功 2失败
     */
    @ApiModelProperty("发送结果：1 成功 2失败")
    private Integer sendResult;

    /**
     * 微信单号(付款返回的订单号)
     */
    @ApiModelProperty("微信单号(付款返回的订单号)")
    private String sendListid;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String headimgurl;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;
}
