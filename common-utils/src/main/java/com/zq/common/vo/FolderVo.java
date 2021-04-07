package com.zq.common.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderVo {

    /**
     * 目录id
     */
    private Integer id;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 目录名称
     */
    private String name;

    /**
     * URL KEY
     */
    private String key;

    /**
     * 模板路径
     */
    private String path;

    /**
     * 描述、说明
     */
    private String content;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态： 1.显示  2.隐藏
     */
    private Integer status;

    /**
     * 类型： 1.普通目录 2.a标签  3.a标签_blank  4 直接加载url信息
     */
    private Integer type;

    /**
     * 跳转地址
     */
    private String jumpUrl;

    /**
     * 素材类型 （对应sys_dict_detail 表）
     */
    private Integer materialType;

    /**
     * 站点ID
     */
    private Integer siteId;


    /**
     * 站点标题
     */
    private String seoTitle;

    /**
     * 站点内容
     */
    private String seoKeywords;

    /**
     * 站点描述
     */
    private String seoDescription;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 更新者id
     */
    private Integer updateId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建者id
     */
    private Integer createId;
}
