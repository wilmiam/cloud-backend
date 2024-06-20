package com.zq.common.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页查询结果
 *
 * @author wilmiam
 * @since 2021-07-09 18:13
 */
@ApiModel("分页查询结果")
@Data
@JsonPropertyOrder({"start", "size", "total", "rows"})
public class PageVo<T> {

    @ApiModelProperty("本页记录在所有记录中的起始位置")
    private int start;

    @ApiModelProperty("每页记录条数")
    private int size;

    @ApiModelProperty("总记录条数")
    private long total;

    @ApiModelProperty("当前页数据")
    private List<T> rows;

    public static <E> PageVo<E> ofReqVo(PageReqVo reqVo, List<E> rows, long total) {
        PageVo<E> pageVo = new PageVo<>();
        pageVo.setSize(reqVo.getSize());
        pageVo.setStart(reqVo.getOffset());
        pageVo.setTotal(total);
        pageVo.setRows(rows);
        return pageVo;
    }

}
