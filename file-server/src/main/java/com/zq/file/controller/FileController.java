package com.zq.file.controller;

import com.zq.common.utils.AssertUtils;
import com.zq.common.utils.UploadUtils;
import com.zq.common.vo.ResultVo;
import com.zq.common.vo.UploadVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping(value = "/file/uploadFile")
    public ResultVo<String> uploadFile(UploadVo file) {
        log.info("文件入口上传开始：{}", "");
        AssertUtils.hasText(file.getSystemName(), "字段：systemName 系统名不能为空");
        return ResultVo.success(UploadUtils.saveFile(file.getFile(), file.getSystemName()));
    }

    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping(value = "/file/uploadImage")
    public ResultVo<String> uploadImage(UploadVo file) {
        log.info("图片入口上传开始：{}", "");
        AssertUtils.hasText(file.getSystemName(), "字段：systemName 系统名不能为空");
        return ResultVo.success(UploadUtils.saveImg(file.getFile(), file.getSystemName()));
    }

    @GetMapping(value = "/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
    public BufferedImage getImage(HttpServletRequest request) throws IOException {
        try (InputStream is = new FileInputStream(request.getRequestURI())) {
            return ImageIO.read(is);
        }
    }

    @GetMapping(value = "/file/**")
    public ResponseEntity<Resource> download(HttpServletRequest request) {
        String contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(request.getRequestURI())
                .build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(request.getRequestURI()));
    }

}
