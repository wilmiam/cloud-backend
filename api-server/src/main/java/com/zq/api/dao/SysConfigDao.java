package com.zq.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zq.api.entity.SysConfig;
import org.springframework.stereotype.Repository;

/**
 * 系统配置表(SysConfig)表数据库访问层
 *
 * @author wilmiam
 * @since 2020-10-14 11:52:04
 */
@Repository
public interface SysConfigDao extends BaseMapper<SysConfig> {

}