package com.zq.cas.service;

import cn.hutool.core.util.StrUtil;
import com.zq.cas.feignclient.AdminFeignClient;
import com.zq.common.utils.AssertUtils;
import com.zq.common.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class FyUserManager {

    @Autowired
    private AdminFeignClient feignClient;


    /**
     * fy用户单点登陆
     *
     * @param username
     * @return
     */
    public ResultVo casLogin(String username) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        ResultVo resultVo = feignClient.casLogin(params);
        AssertUtils.isTrue(resultVo.isSuccess(), resultVo.getErrMsg());
        return resultVo;
    }


    /**
     * fy用户登出
     *
     * @param token
     */
    public void logout(String token) {
        if (StrUtil.isNotBlank(token) && !"undefined".equals(token)) {
            feignClient.logout();
        }
    }

}
