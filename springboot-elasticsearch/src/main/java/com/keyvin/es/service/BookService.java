package com.keyvin.es.service;

import com.keyvin.es.bean.entity.BookModel;
import com.keyvin.es.bean.response.BookListResp;
import com.keyvin.es.bean.vo.BookAddVo;
import com.keyvin.es.bean.vo.BookListVo;

import java.util.Map;
/**
 * @author weiwh
 * @date 2020/7/12 10:12
 */
public interface BookService {
    /**
     * 列表查询
     */
    BookListResp list(BookListVo vo);
    /**
     * 保存新增
     */
    void save(BookAddVo vo);
    /**
     * 编辑修改
     */
    void update(BookModel bookModel);
    /**
     * 删除单个
     */
    void delete(String id);
    /**
     * 查看详情
     */
    BookModel detail(String id);
}
