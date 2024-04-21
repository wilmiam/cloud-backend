package com.zq.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, Function<Q, List<R>> rowsLoader) {
        return paging(reqVo, rowsLoader, true);
    }

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
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, Function<Q, List<R>> rowsLoader, boolean searchCount) {
        PageHelper.startPage(reqVo.getPage(), reqVo.getSize(), searchCount);
        com.github.pagehelper.Page<R> page = (com.github.pagehelper.Page) rowsLoader.apply(reqVo);
        return PageVo.ofReqVo(reqVo, page.getResult(), Long.valueOf(page.getTotal()).intValue());
    }

    /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓以下是mybatis-plus自带分页插件↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/

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
        return paging(reqVo, mapper, clazz, true);
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
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Class<R> clazz, boolean searchCount) {
        R entity;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BusinessException("分页类型转换错误");
        }
        BeanUtil.copyProperties(reqVo, entity);
        Page<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page.setSearchCount(searchCount);
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
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Wrapper<R> lambdaQuery) {
        return paging(reqVo, mapper, lambdaQuery, true);
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
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Wrapper<R> lambdaQuery, boolean searchCount) {
        Page<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page.setSearchCount(searchCount);
        page = mapper.selectPage(page, lambdaQuery);
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
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Class<R> clazz, LambdaQueryWrapper<R> lambdaQuery) {
        return paging(reqVo, mapper, clazz, lambdaQuery, true);
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
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Class<R> clazz, LambdaQueryWrapper<R> lambdaQuery, boolean searchCount) {
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

        Page<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page.setSearchCount(searchCount);
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
        for (int i = 0; i < normal.size(); i++) {
            if (i + 1 >= normal.size()) {
                break;
            }

            ISqlSegment nextSegment = normal.get(i + 1);
            if (nextSegment instanceof SqlKeyword) {
                ISqlSegment sqlSegment = normal.get(i);

                String segment = sqlSegment.getSqlSegment();
                if (segment.startsWith("#{")) {
                    continue;
                }

                String field = StrUtil.toCamelCase(segment.toLowerCase());
                fields.add(field);
            }
        }

        return fields.toArray(new String[]{});
    }

}
