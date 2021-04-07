package com.zq.cms.service;

import com.zq.common.vo.FolderRollPictureVo;
import com.zq.common.vo.ResultVo;
import io.swagger.models.auth.In;

import java.util.List;

public interface FolderRollPictureService {

    /**
     * 分页获取栏目图片
     *
     * @param vo
     * @return
     */
    public ResultVo pageFolderRollPicture(FolderRollPictureVo vo);

    /**
     * 获取栏目图片
     *
     * @param vo
     * @return
     */
    public List<FolderRollPictureVo> getFolderRollPicture(FolderRollPictureVo vo);

    /**
     * 保存栏目图片
     *
     * @param vo
     * @return
     */
    public ResultVo saveFolderRollPicture(FolderRollPictureVo vo);


    /**
     * 根据id, 查询栏目详情
     *
     * @param id
     * @return
     */
    public FolderRollPictureVo getFolderRollPictureById(Integer id);
}
