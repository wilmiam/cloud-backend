package com.zq.user.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zq.common.config.redis.RedisUtils;
import com.zq.common.config.security.ApiTokenUtils;
import com.zq.common.http.HttpRequestUtils;
import com.zq.common.utils.AssertUtils;
import com.zq.common.utils.PagingUtils;
import com.zq.common.vo.ApiTokenVo;
import com.zq.common.vo.PageVo;
import com.zq.user.dao.AppUserDao;
import com.zq.user.entity.AppUser;
import com.zq.user.manager.UserCacheKeys;
import com.zq.user.vo.FindAppUserVo;
import com.zq.user.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author wilmiam
 * @since 2021-07-09 14:37
 */
@CacheConfig(cacheNames = "user")
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserDao userDao;
    private final RedisUtils redisUtils;

    /**
     * 发送手机验证码
     *
     * @param phone
     */
    public void sendCode(String phone) {
        String code = RandomUtil.randomNumbers(6);
        String content = "您的验证码为：" + code + "（5分钟内有效）。为了保障信息安全，如非本人操作请忽略本短信。";
        // TODO 发送验证码
        log.info(">> phone: {}, sendCode: {}, success: ", phone, code);
        redisUtils.setStr(UserCacheKeys.authCodeKey(phone), code, 5);
    }

    /**
     * 重置密码
     *
     * @param vo
     */
    public void resetPassword(LoginVo vo) {
        verificationCode(vo.getPhone(), vo.getVerifyCode());

        AppUser appUser = userDao.selectOne(Wrappers.lambdaQuery(AppUser.builder().phone(vo.getPhone()).build()));

        AssertUtils.notNull(appUser, "手机号不存在");

        appUser.setPassword(DigestUtils.md5DigestAsHex(vo.getPasswd().getBytes()));

        userDao.updateById(appUser);
    }

    public ApiTokenVo phoneLogin(LoginVo vo) {
        AppUser appUser = userDao.selectOne(Wrappers.lambdaQuery(AppUser.builder().phone(vo.getPhone()).build()));

        AssertUtils.notNull(appUser, "手机号不存在");
        verificationCode(vo.getPhone(), vo.getVerifyCode());
        AssertUtils.isTrue(appUser.getStatus() == 0, appUser.getStatus() == 1 ? "账号已冻结" : "账号已删除");

        appUser.setAccessIp(ServletUtil.getClientIP(HttpRequestUtils.getRequest()));
        appUser.setLastLoginTime(DateUtil.date());
        userDao.updateById(appUser);

        return getApiToken(appUser);
    }

    public ApiTokenVo passwdLogin(LoginVo vo) {
        AppUser appUser = userDao.selectOne(Wrappers.lambdaQuery(AppUser.builder().phone(vo.getPhone()).build()));

        AssertUtils.notNull(appUser, "手机号不存在");
        AssertUtils.isTrue(DigestUtils.md5DigestAsHex(vo.getPasswd().getBytes()).equals(appUser.getPassword()), "密码错误");
        AssertUtils.isTrue(appUser.getStatus() == 0, appUser.getStatus() == 1 ? "账号已冻结" : "账号已删除");

        appUser.setAccessIp(ServletUtil.getClientIP(HttpRequestUtils.getRequest()));
        appUser.setLastLoginTime(DateUtil.date());
        userDao.updateById(appUser);

        return getApiToken(appUser);
    }

    /**
     * 判断验证码
     *
     * @param phone
     * @param code
     */
    public void verificationCode(String phone, String code) {
        String cacheCode = redisUtils.getStr(UserCacheKeys.authCodeKey(phone));
        AssertUtils.isTrue(StrUtil.isNotBlank(cacheCode) && cacheCode.equalsIgnoreCase(code), "验证码错误");
        redisUtils.deleteStr(UserCacheKeys.authCodeKey(phone));
    }

    private ApiTokenVo getApiToken(AppUser appUser) {
        ApiTokenVo tokenVo = ApiTokenVo.builder()
                .userId(appUser.getId().toString())
                .account(appUser.getAccount())
                .phone(appUser.getPhone())
                .realname(appUser.getRealname())
                .nickname(appUser.getNickname())
                .gender(appUser.getGender())
                .age(appUser.getAge())
                .avatar(appUser.getAvatar())
                .userType(appUser.getUserType())
                .build();

        String token = ApiTokenUtils.createToken(tokenVo, UserCacheKeys.APP_TOKEN_EXPIRE_MINUTES);
        tokenVo.setToken(token);

        // 缓存登录用户
        redisUtils.setObj(UserCacheKeys.appTokenKey(token), tokenVo, UserCacheKeys.APP_TOKEN_EXPIRE_MINUTES);

        // 重新登录删除前一个token实现单点登录
        String cacheToken = redisUtils.getStr(UserCacheKeys.liveAppTokenKey(appUser.getId()));
        redisUtils.deleteObj(UserCacheKeys.appTokenKey(cacheToken));

        // 限制同一时间同一帐号只能在一个设备上登录
        redisUtils.setStr(UserCacheKeys.liveAppTokenKey(appUser.getId()), token, UserCacheKeys.APP_TOKEN_EXPIRE_MINUTES);

        return tokenVo;
    }

    /**
     * 修改密码
     *
     * @param vo
     */
    public void modifyPasswd(LoginVo vo) {
        verificationCode(vo.getPhone(), vo.getVerifyCode());

        AppUser appUser = userDao.selectOne(Wrappers.lambdaQuery(AppUser.builder().phone(vo.getPhone()).build()));
        AssertUtils.notNull(appUser, "无此用户");

        appUser.setPassword(DigestUtils.md5DigestAsHex(vo.getPasswd().getBytes()));

        userDao.updateById(appUser);
    }

    // @Cacheable
    public AppUser getUserInfo(String userId) {
        return userDao.selectById(userId);
    }

    /**
     * 获取用户列表
     *
     * @param vo
     * @return
     */
    public PageVo<AppUser> getUserList(FindAppUserVo vo) {
        LambdaQueryWrapper<AppUser> lambdaQuery = Wrappers.lambdaQuery(AppUser.class);
        lambdaQuery.orderByAsc(AppUser::getId);

        if (StrUtil.isNotBlank(vo.getAccount())) {
            lambdaQuery.like(AppUser::getAccount, vo.getAccount());
            vo.setAccount(null);
        }
        if (StrUtil.isNotBlank(vo.getPhone())) {
            lambdaQuery.like(AppUser::getPhone, vo.getPhone());
            vo.setPhone(null);
        }

        return PagingUtils.paging(vo, userDao, lambdaQuery);
    }
}
