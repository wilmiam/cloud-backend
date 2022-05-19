package com.zq.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wilmiam
 * @since 2022/1/27 12:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MethodVo {

    private String name;

    private String service;

    private String value;

}
