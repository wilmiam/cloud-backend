package com.zq.cms.dao;

import com.zq.common.vo.FolderRollPictureVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRollPictureDao {

    /**
     * 新增栏目图片
     *
     * @param vo
     * @return
     */
    int insertPicture(FolderRollPictureVo vo);

    /**
     * 更新栏目图片
     *
     * @param vo
     * @return
     */
    int updatePicture(FolderRollPictureVo vo);

    /**
     * 根据id,查询详情
     *
     * @param id
     * @return
     */
    FolderRollPictureVo selectById(@Param("id") Integer id);

    /**
     * 分页查询 栏目图片
     *
     * @param vo
     * @return
     */
    List<FolderRollPictureVo> pageFolderRollPicture(FolderRollPictureVo vo);

    /**
     * 统计总数
     *
     * @param vo
     * @return
     */
    int countFolderRollPicture(FolderRollPictureVo vo);
}
