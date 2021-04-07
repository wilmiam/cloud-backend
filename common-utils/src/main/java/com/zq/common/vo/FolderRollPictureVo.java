package com.zq.common.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderRollPictureVo extends PageReqVo{

    /**
     * 栏目轮播图
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
     * 目录名称
     */
    private String folderName;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态： 1.显示  2.隐藏
     */
    private Integer status;

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 网络图片路径
     */
    private String imageNetUrl;

    /**
     * 链接地址
     */
    private String url;

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
