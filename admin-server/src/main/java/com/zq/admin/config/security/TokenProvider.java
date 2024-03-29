package com.zq.admin.config.security;/*
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

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.zq.common.config.redis.RedisUtils;
import com.zq.common.config.security.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author /
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private final RedisUtils redisUtils;
    private final SecurityProperties properties;

    public static final String AUTHORITIES_KEY = "auth";
    private static Key key;
    private static SignatureAlgorithm signatureAlgorithm;

    @Override
    public void afterPropertiesSet() {
        signatureAlgorithm = SignatureAlgorithm.HS512;
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(properties.getBase64Secret());
        key = new SecretKeySpec(keyBytes, signatureAlgorithm.getJcaName());
    }

    public static String createToken(Authentication authentication) {
        return Jwts.builder()
                .claim(AUTHORITIES_KEY, authentication.getName())
                .setSubject(authentication.getName())
                .signWith(signatureAlgorithm, key)
                // 加入ID确保生成的 Token 都不一致
                .setId(IdUtil.simpleUUID())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(properties.getBase64Secret()))
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        User principal = new User(claims.getSubject(), "******", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }

    /**
     * @param token 需要检查的token
     */
    public void checkRenewal(String token) {
        // 判断是否续期token,计算token的过期时间
        long time = redisUtils.getExpire(properties.getOnlineKey() + token) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= properties.getDetect()) {
            long renew = time + properties.getRenew();
            redisUtils.expire(properties.getOnlineKey() + token, renew, TimeUnit.MILLISECONDS);
        }
    }

    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.isBlank(bearerToken)) {
            return null;
        }
        if (bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }

}
