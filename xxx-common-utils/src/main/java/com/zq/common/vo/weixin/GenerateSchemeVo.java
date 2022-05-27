package com.zq.common.vo.weixin;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author wilmiam
 * @since 2022/5/5 9:56
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateSchemeVo {

    @ApiModelProperty("微信APPID")
    private String appId;

    /**
     * path：通过 scheme 码进入的小程序页面路径，必须是已经发布的小程序存在的页面，不可携带 query。path 为空时会跳转小程序主页。
     * query：通过 scheme 码进入小程序时的 query，最大1024个字符，只支持数字，大小写英文以及部分特殊字符：`!#$&'()*+,/:;=?@-._~%``
     * env_version：要打开的小程序版本。正式版为"release"，体验版为"trial"，开发版为"develop"，仅在微信外打开时生效。
     */
    @ApiModelProperty("跳转到的目标小程序信息。")
    private Map<String, Object> jumpWxa;

    @ApiModelProperty("到期失效的 scheme 码失效类型，失效时间：0，失效间隔天数：1")
    private Integer expireType;

    @ApiModelProperty("到期失效的 scheme 码的失效时间，为 Unix 时间戳。生成的到期失效 scheme 码在该时间前有效。最长有效期为30天。expire_type 为 0 时必填")
    private Long expireTime;

    @ApiModelProperty("到期失效的 scheme 码的失效间隔天数。生成的到期失效 scheme 码在该间隔时间到达前有效。最长间隔天数为30天。 expire_type 为 1 时必填")
    private Integer expireInterval;

}