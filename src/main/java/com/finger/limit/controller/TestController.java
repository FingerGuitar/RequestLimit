package com.finger.limit.controller;

import com.finger.limit.annotation.RequestLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
 
/**
 * create by: lixh
 * description: 测试
 * create time: 2021/8/4 9:34
 * @return
 */
@RestController
public class TestController {
 
    @GetMapping("/test")
    @RequestLimit(maxCount = 3,second = 60)
    public String test() {
        return "成功";
    }
 
 
 
}