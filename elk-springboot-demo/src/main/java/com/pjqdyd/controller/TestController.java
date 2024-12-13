package com.pjqdyd.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**   
 * @Description:  [测试输出日志Controller层]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 输出日志测试
     * @return
     */
    @GetMapping("/outputLog")
    public String testOutputLog(){
        log.info("用户名: {}", "admin");
        log.info("信息: {}", "这是一条信息日志");
        log.warn("警告: {}", "这是一条警告日志");
        log.error("错误: {}", "这是一条错误日志");
        return "success";
    }

}
