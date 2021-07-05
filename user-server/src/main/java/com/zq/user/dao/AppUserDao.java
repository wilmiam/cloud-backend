package com.zq.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zq.common.entity.AppUser;
import org.springframework.stereotype.Repository;

/**
 * 用户表(TAppUser)表数据库访问层
 *
 * @author wilmiam
 * @since 2021-07-05 10:52:18
 */
@Repository
public interface AppUserDao extends BaseMapper<AppUser> {

}
