package com.zq.cms.dao;

import com.zq.common.vo.AdviceFeedbackVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdviceFeedbackDao {

    /**
     * 新增意见反馈
     *
     * @param vo
     * @return
     */
    int insertAdvice(AdviceFeedbackVo vo);

    /**
     * 更新意见反馈
     *
     * @param vo
     * @return
     */
    int updateAdvice(AdviceFeedbackVo vo);


    /**
     * 根据id,查询详情
     *
     * @param id
     * @return
     */
    AdviceFeedbackVo selectById(@Param("id") Integer id);

    /**
     * 分頁查詢
     *
     * @param vo
     * @return
     */
    List<AdviceFeedbackVo> pageAdviceFeedback(AdviceFeedbackVo vo);

    /**
     * 统计总数
     *
     * @param vo
     * @return
     */
    int countAdviceFeedback(AdviceFeedbackVo vo);

}
