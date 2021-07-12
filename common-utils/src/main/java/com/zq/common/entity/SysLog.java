package com.zq.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 系统日志(SysLog)实体类
 *
 * @author makejava
 * @since 2021-07-12 12:43:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sys_log")
public class SysLog {

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long logId;

    /**
     * description
     */
    private String description;

    /**
     * logType
     */
    private String logType;

    /**
     * method
     */
    private String method;

    /**
     * params
     */
    private String params;

    /**
     * requestIp
     */
    private String requestIp;

    /**
     * time
     */
    private Long time;

    /**
     * username
     */
    private String username;

    /**
     * address
     */
    private String address;

    /**
     * browser
     */
    private String browser;

    /**
     * exceptionDetail
     */
    private String exceptionDetail;

    /**
     * createTime
     */
    private Date createTime;

}