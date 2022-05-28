package com.zq.file.controller;

import com.zq.common.utils.AssertUtils;
import io.swagger.annotations.Api;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Api(tags = "访问图片文件")
@RestController
@RequestMapping("/")
public class AccessController {

    @GetMapping(value = "/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(HttpServletRequest request) throws IOException {
        File file = new File(request.getRequestURI());
        AssertUtils.isTrue(file.exists(), "访问图片不存在");

        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return bytes;
    }

    // @GetMapping(value = "/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
    // public BufferedImage getImage(HttpServletRequest request) throws IOException {
    //     try (InputStream is = new FileInputStream(request.getRequestURI())) {
    //         return ImageIO.read(is);
    //     }
    // }

    @GetMapping(value = "/file/**")
    public ResponseEntity<Resource> download(HttpServletRequest request) {
        File file = new File(request.getRequestURI());
        AssertUtils.isTrue(file.exists(), "访问文件不存在");

        String contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(request.getRequestURI())
                .build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
    }

}
