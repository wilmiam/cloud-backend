package com.zq.cms.controller;

import com.zq.cms.service.FolderService;
import com.zq.common.vo.FolderRollPictureVo;
import com.zq.common.vo.ResultVo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @ApiModelProperty("分页获取栏目图片")
    @RequestMapping(value = "/pageFolderRollPicture")
    public ResultVo pageFolderRollPicture(FolderRollPictureVo vo) {
        return null;
    }

    @ApiModelProperty("保存栏目图片")
    @RequestMapping(value = "/saveFolderRollPicture")
    public ResultVo saveFolderRollPicture(FolderRollPictureVo vo){
        return null;
    }

    @ApiModelProperty("根据id, 查询栏目详情")
    @RequestMapping(value = "/getFolderRollPictureById")
    public ResultVo getFolderRollPictureById(FolderRollPictureVo vo){
        return null;
    }
    
    @ApiModelProperty("获取栏目图片")
    @RequestMapping(value = "/getFolderRollPicture")
    public ResultVo getFolderRollPicture(FolderRollPictureVo vo){
        return null;
    }

}
