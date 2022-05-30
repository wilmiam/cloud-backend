package com.zq.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import com.jfinal.wxaapp.api.WxaUserApi;
import com.zq.common.config.redis.RedisUtils;
import com.zq.common.config.security.ApiTokenUtils;
import com.zq.common.http.HttpRequestUtils;
import com.zq.common.utils.AssertUtils;
import com.zq.common.vo.ApiTokenVo;
import com.zq.user.dao.WxAppAccountDao;
import com.zq.user.dao.WxUserDao;
import com.zq.user.entity.WxAppAccount;
import com.zq.user.entity.WxUser;
import com.zq.user.manager.UserCacheKeys;
import com.zq.user.utils.AesCbcUtil;
import com.zq.user.vo.WxLoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author wilmiam
 * @since 2021/8/28 11:02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxUserService {

    private final WxAppAccountDao appAccountDao;
    private final WxUserDao wxUserDao;
    private final RedisUtils redisUtils;

    public ApiTokenVo wxLogin(WxLoginVo vo) {
        WxAppAccount appAccount = appAccountDao.selectOne(Wrappers.lambdaQuery(WxAppAccount.builder().appId(vo.getAppId()).build()));
        AssertUtils.notNull(appAccount, "APPID不存在");

        WxaConfig wxaConfig = new WxaConfig();
        wxaConfig.setAppId(appAccount.getAppId());
        wxaConfig.setAppSecret(appAccount.getAppSecret());
        WxaConfigKit.setWxaConfig(wxaConfig);

        // 获取SessionKey
        // 向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        ApiResult apiResult = WxaUserApi.getSessionKey(vo.getCode());
        // 打印获取的信息
        log.debug("登录获取到的信息: {}", apiResult);
        //判断是否成功
        AssertUtils.isTrue(apiResult.isSucceed(), apiResult.getErrorMsg());

        vo.setUnionId(apiResult.getStr("unionid"));
        vo.setOpenId(apiResult.getStr("openid"));

        String sessionKey = StringEscapeUtils.unescapeJavaScript(apiResult.getStr("session_key"));
        vo.setSessionKey(sessionKey);

        WxUser wxUser = getUser(vo);

        //  查询/添加用户
        wxUser = addUserInfo(wxUser, vo);
        AssertUtils.isTrue(wxUser.getState() == 1, "账号已暂停使用");

        ApiTokenVo appToken = getApiToken(wxUser);
        appToken.setSessionKey(sessionKey);
        return appToken;
    }

    private WxUser getUser(WxLoginVo vo) {
        boolean flag = false;
        WxUser appUser = WxUser.builder().build();
        BeanUtil.copyProperties(vo, appUser);
        appUser.setUnionId(vo.getUnionId());

        // 获取 session_key 和 openid 成功后，对 encryptedData加密数据进行AES解密
        if (StringUtils.isNotBlank(vo.getEncryptData()) && StringUtils.isNotBlank(vo.getSessionKey())) {
            String result = AesCbcUtil.decrypt(vo.getEncryptData(), vo.getSessionKey(), vo.getIvData());
            log.debug(">>> 登录encryptedData解密结果：{}", result);
            if (StringUtils.isNotBlank(result)) {
                JSONObject userInfo = JSON.parseObject(result);
                appUser.setUnionId(userInfo.getString("unionId"));   // 微信开放平台 通用的 unionid
                appUser.setGender(userInfo.getString("gender"));  // 性别 0.未知 1.男 2.女
                appUser.setAvatar(userInfo.getString("avatarUrl"));   // 头像
                appUser.setNickname(userInfo.getString("nickName"));      // 微信昵称
                String address = userInfo.getString("country") + " " + userInfo.getString("province") + " " + userInfo.getString("city");
                appUser.setAddress(address.trim().replace("  ", " "));
                flag = true;
            }
        }

        // 解密失败
        if (!flag && StringUtils.isNotBlank(vo.getAvatar())) {
            appUser.setAvatar(vo.getAvatar());
        }
        if (!flag && StringUtils.isNotBlank(vo.getNickname())) {
            appUser.setNickname(vo.getNickname());
        }
        if (!flag && StringUtils.isNotBlank(vo.getSex())) {
            appUser.setGender(vo.getSex());
        }
        return appUser;
    }

    private WxUser addUserInfo(WxUser user, WxLoginVo vo) {
        WxUser wxUser = wxUserDao.selectOne(Wrappers.lambdaQuery(WxUser.builder().openId(vo.getOpenId()).build()));
        if (wxUser == null) {
            List<WxUser> wxUserList = null;
            if (StringUtils.isNotBlank(vo.getPhone())) {
                wxUserList = wxUserDao.selectList(Wrappers.lambdaQuery(WxUser.builder().phone(vo.getPhone()).build()));
            }
            if (CollUtil.isEmpty(wxUserList) && StringUtils.isNotBlank(user.getUnionId())) {
                wxUserList = wxUserDao.selectList(Wrappers.lambdaQuery(WxUser.builder().unionId(user.getUnionId()).build()));
            }

            if (wxUserList != null && CollUtil.isNotEmpty(wxUserList)) {
                Optional<WxUser> optionalWxUser = wxUserList.stream().max(Comparator.comparing(WxUser::getLastLoginTime));
                optionalWxUser.ifPresent(u -> {
                    user.setUsername(u.getUsername());
                    user.setRealname(u.getRealname());
                    user.setIdCard(u.getIdCard());
                });
            }

            user.setState(1);
            user.setLastLoginTime(DateUtil.date());
            user.setCreateTime(DateUtil.date());
            user.setAccessIp(ServletUtil.getClientIP(HttpRequestUtils.getRequest()));

            wxUserDao.insert(user);

            return user;
        } else {
            if (StringUtils.isBlank(wxUser.getAvatar())) {
                wxUser.setAvatar(user.getAvatar());
            }
            if (StringUtils.isBlank(wxUser.getNickname())) {
                wxUser.setNickname(user.getNickname());
            }
            wxUser.setUnionId(user.getUnionId());
            wxUser.setGender(user.getGender());
            wxUser.setAccessIp(ServletUtil.getClientIP(HttpRequestUtils.getRequest()));
            wxUser.setLastLoginTime(DateUtil.date());
            wxUser.setUpdateTime(DateUtil.date());
            wxUserDao.updateById(wxUser);
            return wxUser;
        }
    }

    private ApiTokenVo getApiToken(WxUser wxUser) {
        ApiTokenVo tokenVo = ApiTokenVo.builder()
                .userId(wxUser.getId())
                .phone(wxUser.getPhone())
                .account(wxUser.getUsername())
                .nickname(StringUtils.isBlank(wxUser.getRealname()) ? wxUser.getNickname() : wxUser.getRealname())
                .build();

        String token = ApiTokenUtils.createToken(tokenVo, UserCacheKeys.APP_TOKEN_EXPIRE_MINUTES);

        tokenVo.setToken(token);

        // 重新登录删除前一个token实现单机登录
        String cacheToken = redisUtils.getStr(UserCacheKeys.liveAppTokenKey(wxUser.getId()));
        redisUtils.deleteObj(UserCacheKeys.appTokenKey(cacheToken));
        redisUtils.deleteStr(UserCacheKeys.liveAppTokenKey(wxUser.getId()));

        // 缓存登录用户
        redisUtils.setObj(UserCacheKeys.appTokenKey(token), tokenVo, UserCacheKeys.APP_TOKEN_EXPIRE_MINUTES);
        // 限制同一时间同一帐号只能在一个设备上登录
        redisUtils.setStr(UserCacheKeys.liveAppTokenKey(wxUser.getId()), token, UserCacheKeys.APP_TOKEN_EXPIRE_MINUTES);

        return tokenVo;
    }

    public String getWxPhone(WxLoginVo vo) {
        String result = AesCbcUtil.decrypt(vo.getEncryptData(), vo.getSessionKey(), vo.getIvData());
        log.debug(">>> 获取手机号encryptedData解密结果：{}", result);
        AssertUtils.hasText(result, "解密失败");
        return JSON.parseObject(result).getString("phoneNumber");
    }

    public void updateWxUserInfo(WxUser vo) {
        WxUser wxUser = wxUserDao.selectById(vo.getId());
        AssertUtils.notNull(wxUser, "用户不存在");

        if (StringUtils.isNotBlank(vo.getPhone()) && !vo.getPhone().contains("*")) {
            wxUser.setPhone(vo.getPhone());
        }
        wxUser.setNickname(vo.getNickname());
        wxUser.setGender(vo.getGender());
        wxUser.setAge(vo.getAge());
        wxUser.setAddress(vo.getAddress());
        wxUser.setState(vo.getState());
        wxUser.setAvatar(vo.getAvatar());
        wxUser.setUpdateTime(DateUtil.date());

        wxUserDao.updateById(wxUser);
    }

    public WxUser getUserInfo(String userId) {
        WxUser wxUser = wxUserDao.selectById(userId);
        AssertUtils.notNull(wxUser, "微信用户不存在");
        wxUser.setPhone(StrUtil.hide(wxUser.getPhone(), 3, 7));
        wxUser.setIdCard(IdcardUtil.hide(wxUser.getIdCard(), 4, 14));
        return wxUser;
    }

}
