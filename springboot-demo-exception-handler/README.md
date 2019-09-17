# springboot-demo-exception-handler
> 此 demo 演示了如何在Spring Boot中进行统一的异常处理，包括了两种方式的处理：第一种对常见API形式的接口进行异常处理，统一封装返回格式；第二种是对模板页面请求的异常处理，统一处理错误页面。

## pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-demo</artifactId>
        <groupId>com.andy</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springboot-demo-exception-handler</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>springboot-demo-exception-handler</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>


        <!--
        在 META-INF/additional-spring-configuration-metadata.json 中配置
        可以去除 application.yml 中自定义配置的红线警告，并且为自定义配置添加 hint 提醒
         -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>

        <!-- 解析 UserAgent 信息 -->
        <dependency>
            <groupId>eu.bitwalker</groupId>
            <artifactId>UserAgentUtils</artifactId>
        </dependency>

        <!-- 热部署 -->
        <dependency>
            <groupId> org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <finalName>springboot-demo-exception-handler</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>


</project>
```

## ApiResponse.java
> 统一的API格式返回封装，里面涉及到的 `BaseException` 和`Status` 这两个类，具体代码见 demo。

```java
package com.andy.exception.handler.model;

import com.andy.exception.handler.constant.Status;
import com.andy.exception.handler.exception.BaseException;
import lombok.Data;

/**
 * @author lianhong
 * @description 通用的 API 接口封装
 * @date 2019/9/17 0017上午 9:01
 */
@Data
public class ApiResponse {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回内容
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 无参构造
     */
    public ApiResponse() {
    }

    /**
     * 全参构造
     *
     * @param code    状态码
     * @param message 返回内容
     * @param data    返回数据
     */
    public ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    /**
     * 构造一个自定义的API返回
     *
     * @param code    状态码
     * @param message 返回内容
     * @param data    返回数据
     * @return ApiResponse
     */
    public static ApiResponse of(Integer code, String message, Object data) {
        return new ApiResponse(code, message, data);
    }

    /**
     * 构造一个成功且带数据的API返回
     *
     * @param data 返回数据
     * @return
     */
    public static ApiResponse ofSuccess(Object data) {
        return ofStatus(Status.OK, data);
    }

    /**
     * 构造一个成功且自定义消息的API返回
     *
     * @param message 返回内容
     * @return ApiResponse
     */
    public static ApiResponse ofMessage(String message) {
        return of(Status.OK.getCode(), Status.OK.getMessage(), null);
    }


    /**
     * 构造一个有状态且带数据的API返回
     *
     * @param status {@link Status}
     * @param data 返回数据
     * @return ApiResponse
     */
    private static ApiResponse ofStatus(Status status, Object data) {
        return of(status.getCode(), status.getMessage(), data);
    }

    /**
     * 构造一个异常且带数据的API返回
     *
     * @param t    异常
     * @param data 返回数据
     * @param <T>  {@link BaseException}的子类
     * @return ApiResponse
     */
    public static <T extends BaseException> ApiResponse ofException(T t, Object data) {
        return of(t.getCode(), t.getMessage(), data);
    }

    /**
     * 构造一个异常且不带数据的API返回
     *
     * @param t   异常
     * @param <T> {@link BaseException}的子类
     * @return ApiResponse
     */
    public static <T extends BaseException> ApiResponse ofException(T t) {
        return ofException(t, null);
    }


}

```

## com.andy.exception.handler.handler.DemoExceptionHandler
```java
package com.andy.exception.handler.handler;

import com.andy.exception.handler.exception.JsonException;
import com.andy.exception.handler.exception.PageException;
import com.andy.exception.handler.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lianhong
 * @description 统一异常处理器
 * @date 2019/9/17 0017上午 9:47
 */
@ControllerAdvice
@Slf4j
public class DemoExceptionHandler {
    private static final String DEFAULT_ERROR_VIEW = "error";
    private static final String MESSAGE = "message";

    /**
     * 统一 json 异常处理
     *
     * @param exception JsonException
     * @return 统一返回json格式
     */
    @ExceptionHandler(value = JsonException.class)
    @ResponseBody
    public ApiResponse jsonErrorHandler(JsonException exception) {
        log.error("【JsonException】：{}", exception.getMessage());
        return ApiResponse.ofException(exception);
    }

    @ExceptionHandler(value = PageException.class)
    @ResponseBody
    public ModelAndView pageErrorHandler(PageException exception) {
        log.error("【DemoPageException】：{}", exception.getMessage());
        ModelAndView view = new ModelAndView();
        view.addObject(MESSAGE, exception.getMessage());
        view.setViewName(DEFAULT_ERROR_VIEW);
        return view;
    }
}

```

## error.html
> 位于 `src/main/resources/template` 目录下

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>统一页面异常处理</title>
</head>
<body>
<h1>统一页面异常处理</h1>
<div th:text="${message}"></div>
</body>
</html>
```

