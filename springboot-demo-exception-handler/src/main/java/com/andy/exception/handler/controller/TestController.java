package com.andy.exception.handler.controller;

import com.andy.exception.handler.constant.Status;
import com.andy.exception.handler.exception.JsonException;
import com.andy.exception.handler.exception.PageException;
import com.andy.exception.handler.model.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lianhong
 * @description
 * @date 2019/9/17 0017上午 9:00
 */
@RestController
public class TestController {
    @GetMapping("/json")
    @ResponseBody
    public ApiResponse jsonException() {
        throw new JsonException(Status.UNKNOW_EXCEPTION);
    }

    @GetMapping("/page")
    @ResponseBody
    public ApiResponse pageException() {
        throw new PageException(Status.UNKNOW_EXCEPTION);
    }
}
