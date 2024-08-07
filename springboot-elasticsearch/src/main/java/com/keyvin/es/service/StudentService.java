package com.keyvin.es.service;

import com.keyvin.es.bean.entity.StudentModel;
import com.keyvin.es.bean.response.StudentListResp;
import com.keyvin.es.bean.vo.StudentAddVo;
import com.keyvin.es.bean.vo.StudentListVo;

/**
 * @author weiwh
 * @date 2020/7/12 10:12
 */
public interface StudentService {
    /**
     * 列表查询
     */
    StudentListResp list(StudentListVo vo);

    /**
     * 保存新增
     */
    void save(StudentAddVo vo);

    /**
     * 编辑修改
     */
    void update(StudentModel bookModel);

    /**
     * 删除单个
     */
    void delete(String id);

    /**
     * 查看详情
     */
    StudentModel detail(String id);
}
