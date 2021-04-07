package com.zq.common.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderNoticeVo {

    /**
     * 栏目公告
     */

    /**
     * 栏目id
     */
    private Integer id;

    /**
     * 目录id
     */
    private Integer folderId;

    /**
     * 类型：
     */
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 内容
     */
    private String content;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态： 1.显示  2.隐藏
     */
    private Integer status;

    /**
     * 删除标记  0.未删除  1.已删除
     */
    private Integer isDeleted;

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
