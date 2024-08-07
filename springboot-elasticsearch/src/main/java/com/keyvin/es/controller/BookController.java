package com.keyvin.es.controller;

import com.keyvin.es.bean.entity.BookModel;
import com.keyvin.es.bean.response.BookListResp;
import com.keyvin.es.bean.vo.BookAddVo;
import com.keyvin.es.bean.vo.BookItemVo;
import com.keyvin.es.bean.vo.BookListVo;
import com.keyvin.es.config.ResultBody;
import com.keyvin.es.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author weiwh
 * @date 2020/7/12 11:17
 */
@Api(tags = "Book接口")
@RestController
@RequestMapping("/book")
public class BookController {
    private Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @ApiOperation(value = "列表，分页查询")
    @ApiResponses(@ApiResponse(code=200, message = "OK", response = BookListResp.class))
    @GetMapping("/list")
    public String bookList(@Validated BookListVo vo){
        BookListResp resp = bookService.list(vo);
        return ResultBody.success(resp);
    }

    @ApiOperation(value = "添加", notes = "请求方式：post请求from表单数据")
    @ApiResponses(@ApiResponse(code=200, message = "OK", response = ResultBody.class))
    @PostMapping("/add")
    public String bookAdd(@Validated BookAddVo vo){
        bookService.save(vo);
        return ResultBody.success();
    }

    @ApiOperation(value = "单个删除")
    @ApiResponses(@ApiResponse(code=200, message = "OK", response = ResultBody.class))
    @DeleteMapping("/delete")
    public String bookDelete(@Validated BookItemVo vo){
        bookService.delete(vo.getId());
        return ResultBody.success();
    }

    @ApiOperation(value = "单个详情")
    @ApiResponses(@ApiResponse(code=200, message = "OK", response = BookModel.class))
    @GetMapping("/item")
    public String bookDetail(@Validated BookItemVo vo){
        BookModel book = bookService.detail(vo.getId());
        return ResultBody.success(book);
    }



}
