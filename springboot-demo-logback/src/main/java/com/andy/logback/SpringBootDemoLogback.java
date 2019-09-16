package com.andy.logback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author lianhong
 * @description
 * @date 2019/9/16 0016下午 5:37
 */
@SpringBootApplication
@Slf4j
public class SpringBootDemoLogback {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoLogback.class, args);
        int length = context.getBeanDefinitionNames().length;
        log.trace("【trace】spring boot 启动初始化了{}个 Bean",length);
        log.debug("【debug】spring boot 启动初始化了{}个 Bean",length);
        log.info("【info】spring boot 启动初始化了{}个 Bean",length);
        log.warn("【warn】spring boot 启动初始化了{}个 Bean",length);
        log.error("【error】spring boot 启动初始化了{}个 Bean",length);

        try {
            int i = 0;
            int j = 1/i;
        } catch (Exception e) {
            log.error("【SpringBootDemoLogbackApplication】启动异常：",e);
        }

    }
}
