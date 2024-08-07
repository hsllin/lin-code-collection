package com.keyvin.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.keyvin.es.bean.entity.BookModel;
import com.keyvin.es.bean.entity.StudentModel;
import com.keyvin.es.bean.response.BookListResp;
import com.keyvin.es.bean.response.ResponseEnum;
import com.keyvin.es.bean.response.StudentListResp;
import com.keyvin.es.bean.response.StudentResp;
import com.keyvin.es.bean.vo.StudentAddVo;
import com.keyvin.es.bean.vo.StudentListVo;
import com.keyvin.es.exception.CustomException;
import com.keyvin.es.service.StudentService;
import com.keyvin.es.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {
    private static final String INDEX_NAME = "student";
    private static final String INDEX_TYPE = "st";
    @Autowired
    private RestHighLevelClient client;


    @Override
    public StudentListResp list(StudentListVo vo) {
        int pageNo = vo.getPageNo();
        int pageSize = vo.getPageSize();
        //分页
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageNo - 1);
        sourceBuilder.size(pageSize);
        sourceBuilder.sort("schoolTime", SortOrder.DESC);
        //查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(vo.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", vo.getKeyword()));
        }
        sourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);
        searchRequest.source(sourceBuilder);

        //处理结果
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            RestStatus restStatus = searchResponse.status();
            if (restStatus != RestStatus.OK) {
                return null;
            }
            List<StudentResp> list = new ArrayList<>();
            SearchHits searchHits = searchResponse.getHits();
            for (SearchHit hit : searchHits.getHits()) {
                String source = hit.getSourceAsString();
                StudentResp student = JSON.parseObject(source, StudentResp.class);
                list.add(student);
            }
            TotalHits totalHits = searchHits.getTotalHits();
            StudentListResp resp = new StudentListResp();
            resp.setPageNo(pageNo);
            resp.setPageSize(pageSize);
            resp.setTotalHits(totalHits.value);
            resp.setList(list);
            TimeValue took = searchResponse.getTook();
            log.info("查询成功！请求参数: {}, 用时{}毫秒", searchRequest.source().toString(), took.millis());
            return resp;

        } catch (IOException e) {
            log.error("查询失败！原因: ", e);
            throw new CustomException(ResponseEnum.INNER_SERVER_ERROR.getCode());
        }
    }

    @Override
    public void save(StudentAddVo vo) {
        Map<String, Object> jsonMap = new HashMap<>();
        String id = Utils.getUuid();
        jsonMap.put("id", id);
        jsonMap.put("name", vo.getName());
        jsonMap.put("schoolTime", vo.getSchoolTime());
        jsonMap.put("age", vo.getAge());
        jsonMap.put("address", vo.getAddress());
        jsonMap.put("status", vo.getStatus());
        jsonMap.put("content", vo.getContent());
        jsonMap.put("schoolName", vo.getSchoolName());

        IndexRequest indexRequest = new IndexRequest(INDEX_NAME, INDEX_TYPE, id);
        indexRequest.source(jsonMap);

        client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse response) {
                String index = response.getIndex();
                String id = response.getId();
                long version = response.getVersion();
                log.info("Index: {}, Id: {}, Version: {}", index, id, version);

                if (response.getResult() == DocWriteResponse.Result.CREATED) {
                    log.info("插入");
                } else if (response.getResult() == DocWriteResponse.Result.UPDATED) {
                    log.info("更新");
                }
                ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                    log.warn("部分分片写入成功");
                }
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        String reason = failure.reason();
                        log.warn("失败原因: {}", reason);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public void update(StudentModel bookModel) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("content", bookModel.getContent());
        UpdateRequest request = new UpdateRequest(INDEX_NAME, INDEX_TYPE, bookModel.getId());
        request.doc(jsonMap);
        try {
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("更新失败！原因: {}", e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        DeleteRequest request = new DeleteRequest(INDEX_NAME, INDEX_TYPE, id);
        try {
            DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
            if (deleteResponse.status() == RestStatus.OK) {
                log.info("删除成功！id: {}", id);
            }
        } catch (IOException e) {
            log.error("删除失败！原因: {}", e.getMessage(), e);
        }
    }

    @Override
    public StudentModel detail(String id) {
        GetRequest getRequest = new GetRequest(INDEX_NAME, INDEX_TYPE, id);
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                String source = getResponse.getSourceAsString();
                StudentModel student = JSON.parseObject(source, StudentModel.class);
                return student;
            }
        } catch (IOException e) {
            log.error("查看失败！原因: {}", e.getMessage(), e);
        }
        return null;
    }
}
