package com.zq.common.vo;

import cn.hutool.core.util.StrUtil;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wilmiam
 * @since 2022-08-12 11:45
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdVo {

    private String id;

    private String note;

    private Set<String> ids;

    public Set<String> getIds() {
        if (ids == null) {
            ids = new HashSet<>();
        }
        if (StrUtil.isNotBlank(id)) {
            ids.add(id);
        }
        return ids;
    }

}
