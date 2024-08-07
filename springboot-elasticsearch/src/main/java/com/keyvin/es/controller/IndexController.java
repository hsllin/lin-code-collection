package com.keyvin.es.controller;

import com.keyvin.es.bean.entity.StudentModel;
import com.keyvin.es.config.ResultBody;
import com.keyvin.es.service.ElasticsearchService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author weiwh
 * @date 2020/7/19 9:38
 */
@Api(tags = "es index接口")
@RestController
@RequestMapping("/index")
public class IndexController {
    private Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ElasticsearchService elasticsearchService;

    @PostMapping("/add")
    public String createIndex(@RequestParam String indexName) throws IOException {
        boolean flag = elasticsearchService.createIndex(indexName);
        return ResultBody.success(flag);
    }

    @PostMapping("/addByMapping")
    public String createIndexByMapping(String indexName) throws IOException {
        boolean flag = elasticsearchService.createIndexByMapping(indexName);
        return ResultBody.success(flag);
    }

    @DeleteMapping("/delete")
    public String deleteIndex(@RequestParam String indexName) throws IOException {
        boolean flag = elasticsearchService.deleteIndex(indexName);
        return ResultBody.success(flag);
    }

    @GetMapping("/exist")
    public String existsIndex(@RequestParam String indexName) throws IOException {
        boolean flag = elasticsearchService.existsIndex(indexName);
        return ResultBody.success(flag);
    }

    @GetMapping("/addStudent")
    public String saveStudent(StudentModel model) {
        elasticsearchService.saveStudent(model);
        return ResultBody.success();
    }


}
