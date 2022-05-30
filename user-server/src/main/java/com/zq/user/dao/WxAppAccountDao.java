package com.zq.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zq.user.entity.WxAppAccount;
import org.springframework.stereotype.Repository;

/**
 * 微信APP账号(WxAppAccount)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-08 16:46:50
 */
@Repository
public interface WxAppAccountDao extends BaseMapper<WxAppAccount> {

}
