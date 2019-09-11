package com.andy.admin.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lianhong
 * @description
 * @date 2019/9/11 0011下午 8:26
 */
@EnableAdminServer
@SpringBootApplication
public class SpringBootDemoAdminServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoAdminServer.class,args);
    }
}
