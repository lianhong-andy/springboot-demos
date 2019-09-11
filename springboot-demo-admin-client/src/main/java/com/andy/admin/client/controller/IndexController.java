package com.andy.admin.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lianhong
 * @description
 * @date 2019/9/11 0011下午 8:01
 */
@RestController
public class IndexController {
    @GetMapping(value = {"", "/"})
    public String index() {
        return "This is a Spring Boot Admin Client.";
    }
}
