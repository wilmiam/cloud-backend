package com.zq.cms.service;

import com.zq.common.vo.AdviceFeedbackVo;
import com.zq.common.vo.ResultVo;

public interface AdviceFeedbackService {

    /**
     * 保存 意见反馈
     *
     * @param vo
     * @return
     */
    public ResultVo saveAdvice(AdviceFeedbackVo vo);

    /**
     * 分页获取 意见反馈
     *
     * @param vo
     * @return
     */
    public ResultVo pageAdviceFeedback(AdviceFeedbackVo vo);

    /**
     * 根据id, 查询意见反馈详情
     *
     * @param id
     * @return
     */
    public AdviceFeedbackVo getAdviceFeedbackById(Integer id);
}
