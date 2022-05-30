package com.zq.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zq.user.entity.WxUser;
import org.springframework.stereotype.Repository;

/**
 * 微信用户表(WxUser)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-08 16:32:42
 */
@Repository
public interface WxUserDao extends BaseMapper<WxUser> {

}
