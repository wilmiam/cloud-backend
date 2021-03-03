package com.zq.file.controller;

import com.zq.common.utils.AssertUtils;
import com.zq.common.utils.UploadUtils;
import com.zq.common.vo.ResultVo;
import com.zq.common.vo.UploadVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "上传相关")
@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @ApiOperation("上传文件")
    @RequestMapping(value = "/uploadFile")
    public ResultVo<String> uploadFile(UploadVo file) {
        log.info("文件入口上传开始：{}", "");
        AssertUtils.hasText(file.getSystemName(), "字段：systemName 系统名不能为空");
        return ResultVo.success(UploadUtils.saveFile(file.getFile(), file.getSystemName()));
    }

    @ApiOperation("上传文件")
    @RequestMapping(value = "/uploadImage")
    public ResultVo<String> uploadImage(UploadVo file) {
        log.info("图片入口上传开始：{}", "");
        AssertUtils.hasText(file.getSystemName(), "字段：systemName 系统名不能为空");
        return ResultVo.success(UploadUtils.saveImg(file.getFile(), file.getSystemName()));
    }

    @ApiOperation("临时文件转正式文件")
    @RequestMapping("/tempToFormal")
    public ResultVo<String> tempToFormal(String filePath) {
        AssertUtils.hasText(filePath, "文件地址不能为空");
        return ResultVo.success(UploadUtils.tempToFormal(filePath));
    }

}
