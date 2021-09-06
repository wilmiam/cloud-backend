package com.zq.common.vo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author CodeAnyWay
 * @since 2019-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadVo {

    private MultipartFile file;

    private String systemName;

}