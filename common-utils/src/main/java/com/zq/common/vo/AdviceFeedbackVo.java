package com.zq.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceFeedbackVo extends PageReqVo{

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userid;

    @ApiModelProperty("用户姓名")
    private String username;

    @ApiModelProperty("用户qq")
    private String qq;

    @ApiModelProperty("邮件")
    private String email;

    @ApiModelProperty("手机号")
    private String telphone;

    @ApiModelProperty("意见反馈内容")
    private String content;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否已读  1.已读  0.未读")
    private Integer isRead;

    @ApiModelProperty("创建者id")
    private Integer createId;

    @ApiModelProperty("对反馈问题的回复内容")
    private String feedbackContent;

    @ApiModelProperty("对反馈问题的回复用户id")
    private Integer feedbackUserId;

    @ApiModelProperty("对反馈问题的回复者名称")
    private String feedbackUserName;

    @ApiModelProperty("回复时间")
    private String feedbackTime;
}
