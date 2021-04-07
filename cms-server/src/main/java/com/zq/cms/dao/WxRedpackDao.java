package com.zq.cms.dao;

import com.zq.common.vo.WxRedpackVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxRedpackDao {

    /**
     * 新增红包记录
     *
     * @param vo
     * @return
     */
    int insertWxRedpack(WxRedpackVo vo);

    /**
     * 更新红包记录
     *
     * @param vo
     * @return
     */
    int updateWxRedpack(WxRedpackVo vo);

    /**
     * 根据id, 获取详情
     *
     * @param id
     * @return
     */
    WxRedpackVo selectById(@Param("id") Integer id);

    /**
     * 分页获取红包记录
     *
     * @param vo
     * @return
     */
    List<WxRedpackVo> pageWxRedpack(WxRedpackVo vo);

    /**
     * 统计总数
     *
     * @param vo
     * @return
     */
    int countWxRedpack(WxRedpackVo vo);
}
