package com.andy.orm.mybatis.MapperAndPage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029上午 9:20
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.andy.orm.mybatis.MapperAndPage.mapper"})
public class SpringBootDemoOrmMybatisMapperAndPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoOrmMybatisMapperAndPageApplication.class, args);
    }
}
