package com.zq.common.vo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wilmiam
 * @since 2022-08-12 11:45
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
