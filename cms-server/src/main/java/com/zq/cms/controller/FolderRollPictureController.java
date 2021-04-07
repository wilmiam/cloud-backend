package com.zq.cms.controller;

import com.zq.cms.service.FolderRollPictureService;
import com.zq.common.vo.FolderRollPictureVo;
import com.zq.common.vo.ResultVo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cms/folderRollPicture")
public class FolderRollPictureController {

    @Autowired
    private FolderRollPictureService folderRollPictureService;

    @ApiModelProperty("分页获取栏目图片")
    @RequestMapping(value = "/pageFolderRollPicture")
    public ResultVo pageFolderRollPicture(FolderRollPictureVo vo) {
        return folderRollPictureService.pageFolderRollPicture(vo);
    }

    @ApiModelProperty("保存栏目图片")
    @RequestMapping(value = "/saveFolderRollPicture")
    public ResultVo saveFolderRollPicture(FolderRollPictureVo vo){
        return folderRollPictureService.saveFolderRollPicture(vo);
    }

    @ApiModelProperty("根据id, 查询栏目详情")
    @RequestMapping(value = "/getFolderRollPictureById")
    public ResultVo getFolderRollPictureById(FolderRollPictureVo vo){
        return ResultVo.success(folderRollPictureService.getFolderRollPictureById(vo.getId()));
    }

    @ApiModelProperty("获取栏目图片")
    @RequestMapping(value = "/getFolderRollPicture")
    public ResultVo getFolderRollPicture(FolderRollPictureVo vo){
        vo.setPage(0);
        vo.setSize(5);
        vo.setStatus(1);
        return ResultVo.success(folderRollPictureService.getFolderRollPicture(vo));
    }

}
