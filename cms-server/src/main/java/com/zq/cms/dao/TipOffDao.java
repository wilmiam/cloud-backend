package com.zq.cms.dao;

import com.zq.common.vo.TipOffVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface TipOffDao {

    /**
     * 新增举报事件
     *
     * @param vo
     * @return
     */
    int insertTipOff(TipOffVo vo);

    /**
     * 更新举报事件
     *
     * @param vo
     * @return
     */
    int updateTipOff(TipOffVo vo);

    /**
     * 根据id,查询详情
     *
     * @param id
     * @return
     */
    TipOffVo selectById(@Param("id") Integer id);
}
