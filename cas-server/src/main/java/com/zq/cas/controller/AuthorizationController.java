/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.zq.cas.controller;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zq.cas.config.JumpConfig;
import com.zq.cas.config.SpringCasAutoconfig;
import com.zq.cas.service.FyUserManager;
import com.zq.common.annotation.AnonymousAccess;
import com.zq.common.annotation.rest.AnonymousPostMapping;
import com.zq.common.config.security.SecurityProperties;
import com.zq.common.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权接口")
public class AuthorizationController {

    private final JumpConfig jumpConfig;
    private final SpringCasAutoconfig casAutoconfig;
    private final SecurityProperties properties;
    private final FyUserManager fyUserManager;

    @RequestMapping("/login")
    @AnonymousAccess
    public void casLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        if (!username.contains("@")) {
            username = username + "@gxfy.com";
        }
        log.info("登录用户：" + username);

        ResultVo resultVo = fyUserManager.casLogin(username);
        if (resultVo.isSuccess()) {
        }

        JSONObject object = JSON.parseObject(JSON.toJSONString(resultVo.getData()));

        String sessionId = request.getRequestedSessionId();

        String systemTag = request.getParameter("systemTag");
        String domain = jumpConfig.getDomain(systemTag);

        response.sendRedirect(domain + "/#/verifyLogin?" + properties.getHeader() + "=" + object.getString("token") + "&JSESSIONID=" + sessionId);
    }

    @ApiOperation("退出登录")
    @RequestMapping(value = "/logout")
    @ResponseBody
    @AnonymousAccess
    public ResultVo<String> logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        fyUserManager.logout(request.getHeader(properties.getHeader()));
        String systemTag = request.getParameter("systemTag");

//        String ticket = request.getParameter("ticket");
        request.removeAttribute("ticket");
//        if (StrUtil.isNotBlank(ticket)) {
//            request.getParameter("ticket").trim();
//        }

        session.invalidate();
        request.getSession().invalidate();

        String service = URLUtil.encode(casAutoconfig.getCasServerLoginUrl() + "?systemTag=" + systemTag);
        String logoutUrl = casAutoconfig.getCasServerUrlPrefix() + "/logout?service=" + service;
        return ResultVo.success(logoutUrl);
    }


    //用于旧系统退出单点登录
    @ApiOperation("旧系统退出单点登录")
    @RequestMapping(value = "/cas/logout")
    @AnonymousAccess
    public void casLogout(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        fyUserManager.logout(request.getHeader(properties.getHeader()));
        request.removeAttribute("ticket");
        String service = request.getParameter("service");
        session.invalidate();
        request.getSession().invalidate();
        String logoutUrl = casAutoconfig.getCasServerUrlPrefix() + "/logout?service=" + service;
        response.sendRedirect(logoutUrl);
    }


    @ApiOperation("销毁token")
    @RequestMapping(value = "/removeToken")
    @ResponseBody
    @AnonymousAccess
    public ResultVo removeToken(HttpServletRequest request) {
        fyUserManager.logout(request.getHeader(properties.getHeader()));
        return ResultVo.success();
    }

    @ApiOperation("获取token")
    @AnonymousPostMapping("/getToken")
    public ResultVo removeToken(@RequestParam("username") String username) {
        if (!username.contains("@")) {
            username = username + "@gxfy.com";
        }
        ResultVo resultVo = fyUserManager.casLogin(username);
        JSONObject object = JSON.parseObject(JSON.toJSONString(resultVo.getData()));
        return ResultVo.success(object.getString("token"));
    }


}
