package com.zq.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class UploadUtils {

    private final static String DATE_FORMAT = "/yyyyMM/dd/";

    /**
     * 保存图片
     *
     * @return
     */
    public static String saveImg(MultipartFile file, String systemName) {
        try {
            String originalFilename = file.getOriginalFilename();
            //取文件扩展名
            String ext = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
            AssertUtils.isImgExt(ext, "图片格式不正确");
            //生成新文件名
            String name = UuidUtils.uuidNoDash() + "." + ext;
            String yyyyMMdd = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            File dest = new File("/images/" + systemName + yyyyMMdd + name);
            FileUtils.writeByteArrayToFile(dest, file.getBytes());
            return "/images/" + systemName + yyyyMMdd + name;
        } catch (IOException e) {
            log.error("图保存失败：{}", e.getMessage());
            return "图保存失败";
        }
    }

    /**
     * 保存文件
     *
     * @return
     */
    public static String saveFile(MultipartFile file, String systemName) {
        try {
            String originalFilename = file.getOriginalFilename();
            //取文件扩展名
            String ext = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
            //生成新文件名
            String name = UuidUtils.uuidNoDash() + "." + ext;
            String yyyyMMdd = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            File dest = new File("/file/" + systemName + yyyyMMdd + name);
            FileUtils.writeByteArrayToFile(dest, file.getBytes());
            return "/file/" + systemName + yyyyMMdd + name;
        } catch (IOException e) {
            log.error("文件保存失败：{}", e.getMessage());
            return "文件保存失败";
        }
    }

}
