# spring-boot-demo-log-aop

> 此 demo 主要是演示如何使用 aop 切面对请求进行日志记录，并且记录 UserAgent 信息。

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

    <artifactId>springboot-demo-log-aop</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>springboot-demo-log-aop</name>
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
        <finalName>springboot-demo-log-aop</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>


</project>
```

## AopLog.java
```java
package com.andy.log.aop.aspectj;

import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @author lianhong
 * @description 使用 aop 切面记录请求日志信息
 * @date 2019/9/16 0016下午 8:16
 */
@Aspect
@Component
@Slf4j
public class AopLog {
    private static final String START_TIME = "request-start";

    /**
     * 切入点
     */
    @Pointcut("execution(public * com.andy.log.aop.controller.*Controller.*(..))")
    public void log() {

    }

    /**
     * 前置操作
     *
     * @param point 切入点
     */
    @Before("log()")
    public void beforeLog(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        log.info("【请求URL】:{}", request.getRequestURL());
        log.info("【请求IP】:{}", request.getRemoteAddr());
        log.info("【请求类名】:{},【请求方法名】:{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());

        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("【请求参数】：{}", JSONUtil.toJsonStr(parameterMap));
        Long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);
    }

    /**
     * 环绕操作
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("log()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();
        log.info("【返回值】：{}", JSONUtil.toJsonStr(result));
        return result;
    }

    @AfterReturning("log()")
    public void afterReturning() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        Long st = (Long) request.getAttribute(START_TIME);
        Long et = System.currentTimeMillis();
        log.info("【请求耗时】：{}毫秒",et-st);

        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        log.info("【浏览器类型】：{},【操作系统】：{}，【原始User-Agent】：{}",userAgent.getBrowser().toString()
                ,userAgent.getOperatingSystem().toString(),header);
    }


}

```
## LogController
```java
package com.andy.log.aop.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lianhong
 * @description
 * @date 2019/9/16 0016下午 8:15
 */
@RestController
public class LogController {
    @GetMapping("/log")
    public Dict log(String who) {
        return Dict.create().set("who", StrUtil.isBlank(who) ? "me" : who);
    }
}

```


## 控制台输出
```text
2019-09-16 20:43:06,072 [http-nio-8087-exec-3] INFO  [com.andy.log.aop.aspectj.AopLog] AopLog.java:48 - 【请求URL】:http://localhost:8087/demo/log
2019-09-16 20:43:06,076 [http-nio-8087-exec-3] INFO  [com.andy.log.aop.aspectj.AopLog] AopLog.java:49 - 【请求IP】:0:0:0:0:0:0:0:1
2019-09-16 20:43:06,078 [http-nio-8087-exec-3] INFO  [com.andy.log.aop.aspectj.AopLog] AopLog.java:50 - 【请求类名】:com.andy.log.aop.controller.LogController,【请求方法名】:log
2019-09-16 20:43:06,114 [http-nio-8087-exec-3] INFO  [com.andy.log.aop.aspectj.AopLog] AopLog.java:53 - 【请求参数】：{"who":["andy"]}
2019-09-16 20:43:06,117 [http-nio-8087-exec-3] INFO  [com.andy.log.aop.aspectj.AopLog] AopLog.java:68 - 【返回值】：{"who":"andy"}
2019-09-16 20:43:06,118 [http-nio-8087-exec-3] INFO  [com.andy.log.aop.aspectj.AopLog] AopLog.java:78 - 【请求耗时】：3毫秒
2019-09-16 20:43:06,129 [http-nio-8087-exec-3] INFO  [com.andy.log.aop.aspectj.AopLog] AopLog.java:82 - 【浏览器类型】：CHROME,【操作系统】：WINDOWS_7，【原始User-Agent】：Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36

```