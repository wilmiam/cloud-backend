package com.zq.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zq.common.exception.BusinessException;
import com.zq.common.vo.PageReqVo;
import com.zq.common.vo.PageVo;

import java.util.List;
import java.util.function.Function;

/**
 * @author
 * @since 2019-04-02
 */
public abstract class PagingUtils {

    @SuppressWarnings("all")
    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, Function<Q, List<R>> rowsLoader) {
        PageHelper.startPage(reqVo.getPage(), reqVo.getSize());
        com.github.pagehelper.Page<R> page = (com.github.pagehelper.Page) rowsLoader.apply(reqVo);
        return PageVo.ofReqVo(reqVo, page.getResult(), Long.valueOf(page.getTotal()).intValue());
    }

    public static <R, Q extends PageReqVo> PageVo<R> paging(Q reqVo, BaseMapper<R> mapper, Class<R> clazz) {
        R instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BusinessException("分页类型转换错误");
        }
        BeanUtil.copyProperties(reqVo, instance);
        IPage<R> page = new Page<>(reqVo.getPage(), reqVo.getSize());
        page = mapper.selectPage(page, Wrappers.lambdaQuery(instance));
        return PageVo.ofReqVo(reqVo, page.getRecords(), Long.valueOf(page.getTotal()).intValue());
    }

}
