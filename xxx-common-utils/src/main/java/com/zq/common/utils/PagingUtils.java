package com.zq.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zq.common.vo.PageReqVo;
import com.zq.common.vo.PageVo;

import java.util.List;
import java.util.function.Function;

/**
 * Mybatis 分页工具
 *
 * @author wilmiam
 * @since 2021-07-09 17:59
 */
public class PagingUtils {

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
        R instance = BeanUtil.copyProperties(reqVo, clazz);

        IPage<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page = mapper.selectPage(page, Wrappers.lambdaQuery(instance));
        return PageVo.ofReqVo(reqVo, page.getRecords(), Long.valueOf(page.getTotal()).intValue());
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
    @SuppressWarnings("all")
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, LambdaQueryWrapper<R> lambdaQuery) {
        IPage<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page = mapper.selectPage(page, lambdaQuery);
        return PageVo.ofReqVo(reqVo, page.getRecords(), Long.valueOf(page.getTotal()).intValue());
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
    @SuppressWarnings("all")
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Class<R> clazz, LambdaQueryWrapper<R> lambdaQuery) {
        R instance = BeanUtil.copyProperties(reqVo, clazz);

        IPage<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page = mapper.selectPage(page, lambdaQuery.setEntity(instance));
        return PageVo.ofReqVo(reqVo, page.getRecords(), Long.valueOf(page.getTotal()).intValue());
    }

}
