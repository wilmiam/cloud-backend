package com.zq.cms.service.impl;

import com.zq.cms.dao.AdviceFeedbackDao;
import com.zq.cms.service.AdviceFeedbackService;
import com.zq.common.utils.PagingUtils;
import com.zq.common.vo.AdviceFeedbackVo;
import com.zq.common.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdviceFeedbackServiceImpl implements AdviceFeedbackService {

    @Autowired
    private AdviceFeedbackDao adviceFeedbackDao;

    /**
     * 保存 意见反馈
     *
     * @param vo
     * @return
     */
    @Override
    public ResultVo saveAdvice(AdviceFeedbackVo vo) {
        try {
            if (vo.getId() != null) {
                adviceFeedbackDao.updateAdvice(vo);
            } else {
                adviceFeedbackDao.insertAdvice(vo);
            }
            return ResultVo.success(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVo.fail("系统繁忙！请稍候再试...");
    }

    /**
     * 分页获取 意见反馈
     *
     * @param vo
     * @return
     */
    @Override
    public ResultVo pageAdviceFeedback(AdviceFeedbackVo vo) {
        return ResultVo.success(PagingUtils.paging(vo, adviceFeedbackDao::pageAdviceFeedback));
    }

    /**
     * 根据id, 查询意见反馈详情
     *
     * @param id
     * @return
     */
    @Override
    public AdviceFeedbackVo getAdviceFeedbackById(Integer id) {
        return adviceFeedbackDao.selectById(id);
    }
}
