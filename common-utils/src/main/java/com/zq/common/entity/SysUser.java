package com.zq.common.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 系统用户(SysUser)实体类
 *
 * @author wilmiam
 * @since 2020-09-30 11:19:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUser extends Model<SysUser> {

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private Long deptId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    private String avatarName;

    /**
     * 头像真实路径
     */
    @ApiModelProperty("头像真实路径")
    private String avatarPath;

    /**
     * 是否为admin账号
     */
    @ApiModelProperty("是否为admin账号")
    private Boolean isAdmin;

    /**
     * 状态：1启用、0禁用
     */
    @ApiModelProperty("状态：1启用、0禁用")
    private Boolean enabled;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String createBy;

    /**
     * 更新着
     */
    @ApiModelProperty("更新着")
    private String updateBy;

    /**
     * 修改密码的时间
     */
    @ApiModelProperty("修改密码的时间")
    private Date pwdResetTime;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

}