package com.zq.user.controller.admin;

import com.zq.common.vo.ResultVo;
import com.zq.user.service.UserService;
import com.zq.user.vo.FindAppUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wilmiam
 * @since 2021/7/10 11:06
 */
@Api(tags = "用户相关接口")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user/admin")
public class UserAdminController {

    private final UserService userService;

    @ApiOperation("获取用户列表")
    @PostMapping(value = "/getUserList")
    public ResultVo getUserList(@RequestBody FindAppUserVo vo) {
        return ResultVo.success(userService.getUserList(vo));
    }

}
