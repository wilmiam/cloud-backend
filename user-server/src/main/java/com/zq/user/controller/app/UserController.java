package com.zq.user.controller.app;


import com.zq.common.annotation.Limit;
import com.zq.common.config.limit.LimitType;
import com.zq.common.utils.AssertUtils;
import com.zq.common.utils.ValidateUtil;
import com.zq.common.vo.ApiTokenVo;
import com.zq.common.vo.ResultVo;
import com.zq.user.service.UserService;
import com.zq.user.vo.FindAppUserVo;
import com.zq.user.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author wilmiam
 * @since 2021-07-09 10:38
 */
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user/app")
public class UserController {

    private final UserService userService;

    /**
     * 发送手机验证码
     * 使用第一个参数值作为限流key, 30秒只能访问1次
     *
     * @param phone
     * @return
     */
    @Limit(limitType = LimitType.PARAM, keyParamIndex = 0, period = 1, count = 1, name = "发送手机验证码", errMsg = "请稍后再试!")
    @ApiOperation("发送验证码")
    @GetMapping(value = "/sendCode")
    public ResultVo sendCode(String phone) {
        AssertUtils.hasText(phone, "手机号不能为空");
        AssertUtils.isTrue(ValidateUtil.isMobilePhoneNo(phone), "手机号格式不正确");
        userService.sendCode(phone);
        return ResultVo.success();
    }

    @ApiOperation("重置登录密码")
    @PostMapping(value = "/resetPasswd")
    public ResultVo resetPassword(@RequestBody LoginVo vo) {
        AssertUtils.hasText(vo.getPhone(), "手机号不能为空");
        AssertUtils.isTrue(ValidateUtil.isMobilePhoneNo(vo.getPhone()), "手机号格式不正确");
        AssertUtils.hasText(vo.getVerifyCode(), "请输入验证码");
        userService.resetPassword(vo);
        return ResultVo.success();
    }

    @ApiOperation("修改登录密码")
    @PostMapping(value = "/modifyPasswd")
    public ResultVo modifyPasswd(@RequestBody LoginVo vo) {
        AssertUtils.hasText(vo.getPhone(), "手机号不能为空");
        AssertUtils.isTrue(ValidateUtil.isMobilePhoneNo(vo.getPhone()), "手机号格式不正确");
        AssertUtils.hasText(vo.getVerifyCode(), "请输入验证码");
        AssertUtils.hasText(vo.getPasswd(), "请输入密码");
        userService.modifyPasswd(vo);
        return ResultVo.success();
    }

    @ApiOperation("手机号登录")
    @PostMapping(value = "/phoneLogin")
    public ResultVo<ApiTokenVo> phoneLogin(@RequestBody LoginVo vo) {
        AssertUtils.hasText(vo.getPhone(), "手机号不能为空");
        AssertUtils.hasText(vo.getVerifyCode(), "请输入验证码");
        return ResultVo.success(userService.phoneLogin(vo));
    }

    @ApiOperation("密码登录")
    @PostMapping(value = "/passwdLogin")
    public ResultVo<ApiTokenVo> passwdLogin(@RequestBody LoginVo vo) {
        AssertUtils.hasText(vo.getPhone(), "手机号不能为空");
        AssertUtils.hasText(vo.getPasswd(), "请输入密码");
        return ResultVo.success(userService.passwdLogin(vo));
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/getUserInfo")
    public ResultVo getUserInfo(@RequestParam String userId) {
        return ResultVo.success(userService.getUserInfo(userId));
    }

    @ApiOperation("获取用户列表")
    @PostMapping(value = "/getUserList")
    public ResultVo getUserList(@RequestBody FindAppUserVo vo) {
        return ResultVo.success(userService.getUserList(vo));
    }

}
