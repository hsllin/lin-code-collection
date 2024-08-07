package com.keyvin.es.service;

import com.keyvin.es.bean.entity.StudentModel;
import com.keyvin.es.bean.response.StudentListResp;
import com.keyvin.es.bean.response.SuggestResp;
import com.keyvin.es.bean.vo.StudentListVo;

import java.io.IOException;
import java.util.List;

/**
 * @author weiwh
 * @date 2020/7/12 10:32
 */
public interface ElasticsearchService {

    boolean createIndex(String indexName) throws IOException;

    boolean createIndexByMapping(String indexName) throws IOException;

    boolean deleteIndex(String indexName) throws IOException;

    boolean existsIndex(String indexName) throws IOException;

    void saveStudent(StudentModel model);

    StudentListResp searchData(StudentListVo vo);

    List<SuggestResp> findSuggester(String key);

}
