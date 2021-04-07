package com.zq.common.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipOffVo {

    /**
     * 用户举报
     */

    private Integer id;

    /**
     * 举报人 用户id
     */
    private Integer userid;

    /**
     * 举报类型  1.偷盗 2.违建3.放牧4.未允进入5.其他
     */
    private Integer type;

    /**
     * 举报对象的姓名
     */
    private String person;

    /**
     * 举报事件的发生地点
     */
    private String addr;

    /**
     * 事件发生时间
     */
    private Date happenedTime;

    /**
     * 举报的事件描述
     */
    private String event;

    /**
     * 状态：1 举报上传  2 处理中 3  处理结束
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
