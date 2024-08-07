package com.keyvin.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.keyvin.es.bean.entity.BookModel;
import com.keyvin.es.bean.response.BookListResp;
import com.keyvin.es.bean.response.ResponseEnum;
import com.keyvin.es.bean.vo.BookAddVo;
import com.keyvin.es.bean.vo.BookListVo;
import com.keyvin.es.exception.CustomException;
import com.keyvin.es.service.BookService;
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
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwh
 * @date 2020/7/12 10:14
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private static final String INDEX_NAME = "book_library";
    private static final String INDEX_TYPE = "_doc";

    @Autowired
    private RestHighLevelClient client;

    @Override
    public BookListResp list(BookListVo bookListVo) {
        int pageNo = bookListVo.getPageNo();
        int pageSize = bookListVo.getPageSize();
        //分页
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageNo - 1);
        sourceBuilder.size(pageSize);
        sourceBuilder.sort("publishTime", SortOrder.DESC);
        //查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(bookListVo.getName())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", bookListVo.getName()));
        }
        if (StringUtils.isNotBlank(bookListVo.getAuthor())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("author", bookListVo.getAuthor()));
        }
        if (null != bookListVo.getStatus()) {
            boolQueryBuilder.must(QueryBuilders.termQuery("status", bookListVo.getStatus()));
        }
        if (null != bookListVo.getPublishTime()) {
            boolQueryBuilder.must(QueryBuilders.termQuery("publishTime", bookListVo.getPublishTime()));
        }
        if (StringUtils.isNotBlank(bookListVo.getCategories())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("categories", bookListVo.getCategories()));
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
            List<BookModel> list = new ArrayList<>();
            SearchHits searchHits = searchResponse.getHits();
            for (SearchHit hit : searchHits.getHits()) {
                String source = hit.getSourceAsString();
                BookModel book = JSON.parseObject(source, BookModel.class);
                list.add(book);
            }
            TotalHits totalHits = searchHits.getTotalHits();
            BookListResp resp = new BookListResp();
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
    public void save(BookAddVo vo) {
        Map<String, Object> jsonMap = new HashMap<>();
        String id = Utils.getUuid();
        jsonMap.put("id", id);
        jsonMap.put("name", vo.getName());
        jsonMap.put("author", vo.getAuthor());
        jsonMap.put("category", vo.getCategory());
        jsonMap.put("price", vo.getPrice());
        jsonMap.put("publishTime", vo.getPublishTime());
        jsonMap.put("content", vo.getContent());
        jsonMap.put("status", vo.getStatus());

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
    public void update(BookModel model) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("content", model.getContent());
        UpdateRequest request = new UpdateRequest(INDEX_NAME, INDEX_TYPE, model.getId());
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
    public BookModel detail(String id) {
        GetRequest getRequest = new GetRequest(INDEX_NAME, INDEX_TYPE, id);
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                String source = getResponse.getSourceAsString();
                BookModel book = JSON.parseObject(source, BookModel.class);
                return book;
            }
        } catch (IOException e) {
            log.error("查看失败！原因: {}", e.getMessage(), e);
        }
        return null;
    }
}
