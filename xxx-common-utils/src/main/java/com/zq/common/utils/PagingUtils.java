package com.zq.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zq.common.exception.BusinessException;
import com.zq.common.vo.PageReqVo;
import com.zq.common.vo.PageVo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author
 * @since 2019-04-02
 */
public abstract class PagingUtils {

    /**
     * pagehelper分页插件
     *
     * @param reqVo
     * @param rowsLoader
     * @param <R>
     * @param <Q>
     * @return
     */
    @SuppressWarnings("all")
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, Function<Q, List<R>> rowsLoader) {
        PageHelper.startPage(reqVo.getPage(), reqVo.getSize());
        com.github.pagehelper.Page<R> page = (com.github.pagehelper.Page) rowsLoader.apply(reqVo);
        return PageVo.ofReqVo(reqVo, page.getResult(), Long.valueOf(page.getTotal()).intValue());
    }

    /**
     * mybatis-plus自带分页插件
     *
     * @param reqVo
     * @param mapper
     * @param clazz
     * @param <R>
     * @param <Q>
     * @return
     */
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Class<R> clazz) {
        R entity;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BusinessException("分页类型转换错误");
        }
        BeanUtil.copyProperties(reqVo, entity);
        IPage<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page = mapper.selectPage(page, Wrappers.lambdaQuery(entity));
        return PageVo.ofReqVo(reqVo, page.getRecords(), Long.valueOf(page.getTotal()).intValue());
    }

    /**
     * mybatis-plus自带分页插件
     *
     * @param reqVo
     * @param mapper
     * @param <R>
     * @param <Q>
     * @return
     */
    @SuppressWarnings("all")
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, LambdaQueryWrapper<R> lambdaQuery, Class<R> clazz) {
        R entity;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BusinessException("分页类型转换错误");
        }

        // 已设置在lambdaQuery的字段实体类中不应该重复设置
        String[] whereFields = getWhereFields(lambdaQuery);
        BeanUtil.copyProperties(reqVo, entity, whereFields);

        lambdaQuery.setEntity(entity);

        IPage<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page = mapper.selectPage(page, lambdaQuery);
        return PageVo.ofReqVo(reqVo, page.getRecords(), Long.valueOf(page.getTotal()).intValue());
    }

    /**
     * 获取设置的条件字段名
     *
     * @param lambdaQuery
     * @param <R>
     * @return
     */
    public static <R> String[] getWhereFields(LambdaQueryWrapper<R> lambdaQuery) {
        List<String> fields = new ArrayList<>();

        MergeSegments expression = lambdaQuery.getExpression();
        NormalSegmentList normal = expression.getNormal();
        int index = 0;
        for (ISqlSegment iSqlSegment : normal) {
            if (index % 4 == 0) {
                String field = StrUtil.toCamelCase(iSqlSegment.getSqlSegment());
                fields.add(field);
            }
            index++;
        }

        return fields.toArray(new String[]{});
    }

}
