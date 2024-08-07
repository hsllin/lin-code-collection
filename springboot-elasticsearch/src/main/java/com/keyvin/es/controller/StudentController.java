package com.keyvin.es.controller;

import com.keyvin.es.bean.entity.StudentModel;
import com.keyvin.es.bean.response.StudentListResp;
import com.keyvin.es.bean.vo.StudentAddVo;
import com.keyvin.es.bean.vo.StudentItemVo;
import com.keyvin.es.bean.vo.StudentListVo;
import com.keyvin.es.config.ResultBody;
import com.keyvin.es.service.StudentService;
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
@Api(tags = "Student接口")
@RestController
@RequestMapping("/student")
public class StudentController {
    private Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "列表，分页查询")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = StudentListResp.class))
    @GetMapping("/list")
    public String studentList(@Validated StudentListVo vo) {
        StudentListResp resp = studentService.list(vo);
        return ResultBody.success(resp);
    }

    @ApiOperation(value = "添加", notes = "请求方式：post请求from表单数据")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ResultBody.class))
    @PostMapping("/add")
    public String studentAdd(@Validated StudentAddVo vo) {
        studentService.save(vo);
        return ResultBody.success();
    }

    @ApiOperation(value = "单个删除")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ResultBody.class))
    @DeleteMapping("/delete")
    public String studentDelete(@Validated StudentItemVo vo) {
        studentService.delete(vo.getId());
        return ResultBody.success();
    }

    @ApiOperation(value = "单个详情")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = StudentModel.class))
    @GetMapping("/item")
    public String studentDetail(@Validated StudentItemVo vo) {
        StudentModel book = studentService.detail(vo.getId());
        return ResultBody.success(book);
    }


}
