package com.zq.api.entity;

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
 * (TApiLog)实体类
 *
 * @author wilmiam
 * @since 2021-04-07 14:41:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "t_api_log")
public class ApiLog {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Long id;

    /**
     * APPID
     */
    @ApiModelProperty("APPID")
    private String appId;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private String userId;

    /**
     * 调用方法
     */
    @ApiModelProperty("调用方法")
    private String method;

    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private String version;

    /**
     * 业务参数
     */
    @ApiModelProperty("业务参数")
    private String bizContent;

    /**
     * IP
     */
    @ApiModelProperty("IP")
    private String ip;

    /**
     * 日志类型
     */
    @ApiModelProperty("日志类型")
    private String logType;

    /**
     * 响应信息
     */
    @ApiModelProperty("响应信息")
    private String respMsg;

    /**
     * 堆栈跟踪
     */
    @ApiModelProperty("堆栈跟踪")
    private String stackTrace;

    /**
     * 耗时-毫秒
     */
    @ApiModelProperty("耗时-毫秒")
    private Long timeCost;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
