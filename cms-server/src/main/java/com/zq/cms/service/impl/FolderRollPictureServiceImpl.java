package com.zq.cms.service.impl;

import com.zq.cms.dao.FolderRollPictureDao;
import com.zq.cms.service.FolderRollPictureService;
import com.zq.common.utils.PagingUtils;
import com.zq.common.vo.FolderRollPictureVo;
import com.zq.common.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderRollPictureServiceImpl implements FolderRollPictureService {

    @Autowired
    private FolderRollPictureDao folderRollPictureDao;

    /**
     * 分页获取栏目图片
     *
     * @param vo
     * @return
     */
    @Override
    public ResultVo pageFolderRollPicture(FolderRollPictureVo vo) {
        return ResultVo.success(PagingUtils.paging(vo, folderRollPictureDao::pageFolderRollPicture));
    }

    /**
     * 获取栏目图片
     *
     * @param vo
     * @return
     */
    @Override
    public List<FolderRollPictureVo> getFolderRollPicture(FolderRollPictureVo vo) {
        return folderRollPictureDao.pageFolderRollPicture(vo);
    }

    /**
     * 保存栏目图片
     *
     * @param vo
     * @return
     */
    @Override
    public ResultVo saveFolderRollPicture(FolderRollPictureVo vo) {
        try {
            if (vo.getId() != null) {
                folderRollPictureDao.updatePicture(vo);
            } else {
                folderRollPictureDao.insertPicture(vo);
            }
            return ResultVo.success(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVo.fail("系统繁忙！请稍候再试...");
    }

    /**
     * 根据id, 查询栏目详情
     *
     * @param id
     * @return
     */
    @Override
    public FolderRollPictureVo getFolderRollPictureById(Integer id) {
        return folderRollPictureDao.selectById(id);
    }
}
