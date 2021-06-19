package com.zq.common.utils;

import com.zq.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            throw new BusinessException("图保存失败");
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
            throw new BusinessException("文件保存失败");
        }
    }

    /**
     * 保存临时图片
     *
     * @param file
     * @param systemName
     * @return
     */
    public static String saveTempImage(MultipartFile file, String systemName) {
        try {
            String originalFilename = file.getOriginalFilename();
            //取文件扩展名
            String ext = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
            AssertUtils.isImgExt(ext, "图片格式不正确");
            //生成新文件名
            String name = UuidUtils.uuidNoDash() + "." + ext;
            String yyyyMMdd = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            File dest = new File("/images/temp/" + systemName + yyyyMMdd + name);
            FileUtils.writeByteArrayToFile(dest, file.getBytes());
            return "/images/temp/" + systemName + yyyyMMdd + name;
        } catch (IOException e) {
            log.error("图保存失败：{}", e.getMessage());
            throw new BusinessException("图保存失败");
        }
    }

    /**
     * 保存临时文件
     *
     * @return
     */
    public static String saveTempFile(MultipartFile file, String systemName) {
        try {
            String originalFilename = file.getOriginalFilename();
            //取文件扩展名
            String ext = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
            //生成新文件名
            String name = UuidUtils.uuidNoDash() + "." + ext;
            String yyyyMMdd = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            File dest = new File("/file/temp/" + systemName + yyyyMMdd + name);
            FileUtils.writeByteArrayToFile(dest, file.getBytes());
            return "/file/temp/" + systemName + yyyyMMdd + name;
        } catch (IOException e) {
            log.error("文件保存失败：{}", e.getMessage());
            throw new BusinessException("文件保存失败");
        }
    }

    public static String tempToFormal(String tempPath) {
        Path path = Paths.get(tempPath);
        String formalPath = tempPath.replace("/temp", "");
        File formalFile = new File(formalPath);
        try {
            FileUtils.writeByteArrayToFile(formalFile, Files.readAllBytes(path));
            Files.delete(path);
        } catch (IOException e) {
            log.error("文件转存失败：{}", e.getMessage());
            throw new BusinessException("文件转存失败");
        }
        return formalPath;
    }

    /*public static void main(String[] args) {
        System.out.println(tempToFormal("/images/temp/supply/202101/14/3ca51d7873044443be912e83209c4406.png"));
    }*/

}
