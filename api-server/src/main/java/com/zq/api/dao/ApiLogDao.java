package com.zq.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zq.api.entity.ApiLog;
import org.springframework.stereotype.Repository;

/**
 * (TApiLog)表数据库访问层
 *
 * @author wilmiam
 * @since 2021-04-07 14:42:23
 */
@Repository
public interface ApiLogDao extends BaseMapper<ApiLog> {

}