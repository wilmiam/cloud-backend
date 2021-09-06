package com.zq.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTokenVo {

    private Long userId;

    private String account;

    private String phone;

    private String realname;

    private String nickname;

    private String gender;

    private Integer age;

    private Integer userType;

    private String avatar;

    private String token;

    private String sessionKey;

}
