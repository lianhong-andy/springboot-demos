package com.andy.orm.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lianhong
 * @description
 * @date 2019/10/28 0028下午 6:59
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.andy.orm.mybatis.mapper"})
public class SpringBootDemoOrmMybatis {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoOrmMybatis.class,args);
    }
}
