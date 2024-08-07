package com.keyvin.es.controller;

import com.keyvin.es.bean.response.StudentListResp;
import com.keyvin.es.bean.response.SuggestResp;
import com.keyvin.es.bean.vo.StudentListVo;
import com.keyvin.es.config.ResultBody;
import com.keyvin.es.service.ElasticsearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author weiwh
 * @date 2020/7/12 10:13
 */
@Api(tags = "Search接口")
@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private ElasticsearchService elasticsearchService;

    @ApiOperation(value = "列表页面")
    @ApiResponses(@ApiResponse(code=200, message = "OK"))
    @GetMapping("/bookList")
    public String bookList(){
        return "bookList";
    }

    @ApiOperation(value = "列表页面")
    @ApiResponses(@ApiResponse(code = 200, message = "OK"))
    @GetMapping("/keyv8in")
    public String keyv8in() {
        return "keyv8in";
    }

    @ApiOperation(value = "列表页面")
    @ApiResponses(@ApiResponse(code = 200, message = "OK"))
    @GetMapping("/vueSearch")
    public String vueSearch() {
        return "vueSearch";
    }

    @ApiOperation(value = "列表页面")
    @ApiResponses(@ApiResponse(code = 200, message = "OK"))
    @GetMapping("/vueSearchTest")
    public String vueSearchTest() {
        return "vueSearchTest";
    }

    @ApiOperation(value = "搜索页面")
    @ApiResponses(@ApiResponse(code=200, message = "OK"))
    @GetMapping()
    public String list(){
        return "vueSearch";
    }

    @ApiOperation(value = "搜索补齐、建议")
    @ApiResponses(@ApiResponse(code=200, message = "OK"))
    @ResponseBody
    @GetMapping("/suggester")
    public String suggester(String key){
        List<SuggestResp> list = elasticsearchService.findSuggester(key);
        return ResultBody.success(list);
    }

    @ApiOperation(value = "搜索列表")
    @ApiResponses(@ApiResponse(code=200, message = "OK"))
    @ResponseBody
    @GetMapping("/list")
    public String list(@Validated StudentListVo vo){
        StudentListResp resp = elasticsearchService.searchData(vo);
        return ResultBody.success(resp);
    }

}
