package com.zq.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTokenVo {

    private Long userId;

    private String phone;

    private String name;

    private String nickname;

    private String sex;

    private Integer age;

    private Integer userType;

    private String headPortraitUrl;

    private Integer level;

    private Date dueTime;

    private String token;

    private String sessionKey;

    private String userSig;

}
