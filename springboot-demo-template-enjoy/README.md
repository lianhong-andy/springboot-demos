## SpringBoot-demo-template-enjoy
> 本 demo 主要演示了 Spring Boot 项目如何集成 enjoy 模板引擎。

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

    <artifactId>springboot-demo-template-enjoy</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>springboot-demo-template-enjoy</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <enjoy.version>3.5</enjoy.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.jfinal</groupId>
            <artifactId>enjoy</artifactId>
            <version>${enjoy.version}</version>
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
    </dependencies>

    <build>
        <finalName>springboot-demo-template-enjoy</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

## controller 路径：`springboot-demo-template-enjoy`
### IndexController
```java
package com.andy.template.enjoy.controller;

import cn.hutool.core.util.ObjectUtil;
import com.andy.template.enjoy.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lianhong
 * @description
 * @date 2019/9/18 0018下午 7:39
 */
@RestController
@Slf4j
public class IndexController {
    @GetMapping(value = {"", "/"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        if (ObjectUtil.isNull(user)) {
            mv.setViewName("redirect:/user/login");
        } else {
            mv.addObject(user);
            mv.setViewName("page/index");
        }

        return mv;
    }
}


```

### UserController
```java
package com.andy.template.enjoy.controller;

import cn.hutool.core.util.ObjectUtil;
import com.andy.template.enjoy.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lianhong
 * @description
 * @date 2019/9/18 0018下午 7:39
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request, User user) {
        ModelAndView mv = new ModelAndView();
        if (ObjectUtil.isNull(user)) {
            mv.setViewName("redirect:page/login");
        } else {
            mv.addObject(user);
            mv.setViewName("redirect:/");
        }

        request.getSession().setAttribute("user",user);

        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("page/login");
    }
}

```

## application.yml
```yaml
server:
  port: 8090
  servlet:
    context-path: /demo

```

## 静态页面 
### `templates/common/head.html`
```html
<html lang="en" >
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>springboot-demo-template-thymeleaf</title>
</head>
<body>

</body>
</html>
```

### `templates/page/index.html`
```html
<!DOCTYPE html>
<html lang="en" >
#include("/common/head.html")
<body>
<div id="app" style="margin: 20px 20%">
    welcome,#(user.name)
</div>

</body>
</html>
```

### `templates/page/login.html`
```html
<!DOCTYPE html>
<html lang="en" >
#include("/common/head.html")
<body>
<div id="app" style="margin: 20px 20%">
    <form action="/demo/user/login" method="post">
        <span id="username">username</span><input type="text" name="name" placeholder="username"><br/>
        <span id="password">password</span><input type="password" name="password" placeholder="password"><br/>
        <input type="submit" value="登录">
    </form>
</div>

</body>
</html>
```

## Enjoy 语法糖学习文档

http://www.jfinal.com/doc/6-1