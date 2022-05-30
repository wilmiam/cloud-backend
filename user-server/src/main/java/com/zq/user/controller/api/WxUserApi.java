package com.zq.user.controller.api;

import com.zq.common.utils.AssertUtils;
import com.zq.common.vo.ApiTokenVo;
import com.zq.common.vo.ResultVo;
import com.zq.user.entity.WxUser;
import com.zq.user.service.WxUserService;
import com.zq.user.vo.WxLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "微信登录相关接口")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user/api")
public class WxUserApi {

    private final WxUserService wxUserService;

    @ApiOperation(value = "微信登录")
    @PostMapping(value = "/wxLogin")
    public ResultVo<ApiTokenVo> wxLogin(@RequestBody WxLoginVo vo) {
        AssertUtils.hasText(vo.getCode(), "CODE不能为空");
        AssertUtils.hasText(vo.getAppId(), "APPID不能为空");
        return ResultVo.success(wxUserService.wxLogin(vo));
    }

    @ApiOperation(value = "解密获取微信用户手机号")
    @PostMapping(value = "/getWxPhone")
    public ResultVo getWxPhone(@RequestBody WxLoginVo vo) {
        AssertUtils.hasText(vo.getEncryptData(), "加密数据不能为空");
        AssertUtils.hasText(vo.getIvData(), "解量不能为空");
        AssertUtils.hasText(vo.getSessionKey(), "sessionKey不能为空");
        return ResultVo.success(wxUserService.getWxPhone(vo));
    }

    @ApiOperation(value = "微信用户信息更新")
    @PostMapping(value = "/updateWxUserInfo")
    public ResultVo updateWxUserInfo(@RequestBody WxUser vo) {
        AssertUtils.hasText(vo.getId(), "用户ID不能为空");
        wxUserService.updateWxUserInfo(vo);
        return ResultVo.success();
    }

    @ApiOperation(value = "获取微信用户信息")
    @GetMapping(value = "/getWxUserInfo/{userId}")
    public ResultVo getWxUserInfo(@PathVariable String userId) {
        AssertUtils.hasText(userId, "用户ID不能为空");
        return ResultVo.success(wxUserService.getUserInfo(userId));
    }

}
