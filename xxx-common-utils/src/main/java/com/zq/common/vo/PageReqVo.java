package com.zq.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询请求对象
 *
 * @author wilmiam
 * @since 2021-07-09 18:13
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageReqVo {

    /**
     * 每页显示行数默认为20
     */
    private static final int DEFAULT_SIZE = 20;

    @ApiModelProperty(value = "当前页码, 首页为1", example = "1")
    private int page = 1;

    @ApiModelProperty("每页记录条数")
    private int size = DEFAULT_SIZE;

    @ApiModelProperty(value = "排序字段名", allowEmptyValue = true)
    private String sort;

    @ApiModelProperty(value = "排序方向", allowableValues = "asc,desc", allowEmptyValue = true)
    private String dir;

    public static PageReqVo of(int page, int size) {
        PageReqVo pageReqVo = new PageReqVo();
        pageReqVo.setPage(page);
        pageReqVo.setSize(size);
        return pageReqVo;
    }

    @JsonIgnore
    public int getOffset() {
        return (getPage() - 1) * getSize();
    }

    public int getPage() {
        // default to the first page
        return page > 0 ? page : 1;
    }

    public int getSize() {
        return size > 0 ? size : DEFAULT_SIZE;
    }

}