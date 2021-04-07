package com.zq.cms.controller;

import com.zq.cms.service.AdviceFeedbackService;
import com.zq.common.annotation.rest.AnonymousPostMapping;
import com.zq.common.vo.AdviceFeedbackVo;
import com.zq.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "意见反馈")
@RestController
@RequestMapping("/cms/adviceFeedback")
public class AdviceFeedbackController {

    @Autowired
    private AdviceFeedbackService adviceFeedbackService;

    @ApiModelProperty("分页获取意见反馈列表")
    @AnonymousPostMapping(value = "/pageAdviceFeedback")
    public ResultVo pageAdviceFeedback(AdviceFeedbackVo vo) {
        return adviceFeedbackService.pageAdviceFeedback(vo);
    }

    @ApiModelProperty("保存意见反馈列表")
    @RequestMapping(value = "/saveAdviceFeedback")
    public ResultVo saveAdviceFeedback(AdviceFeedbackVo vo) {
        return adviceFeedbackService.saveAdvice(vo);
    }

    @ApiModelProperty("根据id, 查询意见反馈详情")
    @RequestMapping(value = "/getAdviceFeedbackById")
    public ResultVo getAdviceFeedbackById(AdviceFeedbackVo vo) {
        return ResultVo.success(adviceFeedbackService.getAdviceFeedbackById(vo.getId()));
    }

}
