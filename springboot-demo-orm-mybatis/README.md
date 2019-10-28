# springboot-demo-orm-mybatis

> 此 demo 演示了 Spring Boot 如何与原生的 mybatis 整合，使用了 mybatis 官方提供的脚手架 `mybatis-spring-boot-starter `可以很容易的和 Spring Boot 整合。


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


    <artifactId>springboot-demo-orm-mybatis</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>springboot-demo-orm-mybatis</name>
    <description>Demo project for Spring Boot</description>


    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <mybatis.version>1.3.2</mybatis.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis.version}</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
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

        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>springboot-demo-orm-mybatis</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>


</project>
```

## SpringBootDemoOrmMybatisApplication.java
```java
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

```

## application.yml
```yaml
server:
  port: 8093
  servlet:
    context-path: /demo
spring:
  datasource:
#    url: jdbc:mysql://49.234.41.101:33306/sbt?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8
    url: jdbc:mysql://127.0.0.1:3306/sbt?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.zaxxer.hikari.HikariDataSource
    initialization-mode: always
    continue-on-error: true
    schema:
    - "classpath:db/schema.sql"
    data:
    - "classpath:db/data.sql"
    hikari:
      minimum-idle: 5
      connection-timeout: 30000
      connection-test-query: SELECT 1 FROM DUAL
      maximum-pool-size: 20
      auto-commit: true
      idle-timeout: 30000
      pool-name: SpringBootDemoHikeriCP
      max-lifetime: 60000
logging:
  level:
    com.andy: debug
    com.andy.orm.mybatis.mapper: trace
mybatis:
  configuration:
    # 下划线转驼峰
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mappers/*.xml
  type-aliases-package: com.andy.orm.mybatis.entity
```

## UserMapper.java
```java
package com.andy.orm.mybatis.mapper;

import com.andy.orm.mybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    @Select("select * from orm_user")
    List<User> selectAllUser();

    /**
     * 根据id查询用户
     *
     * @param id
     * @return 当前id的用户，不存在则是 {@code null}
     */
    @Select("select * from orm_user where id = #{id}")
    User selectUserById(@Param("id") Long id);

    /**
     * 保存用户
     *
     * @param user
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    int saveUser(@Param("user") User user);

    /**
     * 删除用户
     *
     * @param id 主键id
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    int deleteById(@Param("id") Long id);
}

```


## UserMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.andy.orm.mybatis.mapper.UserMapper">

    <insert id="saveUser">
        INSERT INTO `orm_user` (`name`,
                                `password`,
                                `salt`,
                                `email`,
                                `phone_number`,
                                `status`,
                                `create_time`,
                                `last_login_time`,
                                `last_update_time`)
        VALUES (#{user.name},
                #{user.password},
                #{user.salt},
                #{user.email},
                #{user.phoneNumber},
                #{user.status},
                #{user.createTime},
                #{user.lastLoginTime},
                #{user.lastUpdateTime})
    </insert>
    
    <delete id="deleteById">
        DELETE
        FROM `orm_user`
        WHERE `id` = #{id}
    </delete>

</mapper>
```

## UserMapperTest
```java
package com.andy.orm.mybatis.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.andy.orm.mybatis.SpringBootDemoMybatisApplicationTest;
import com.andy.orm.mybatis.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lianhong
 * @description
 * @date 2019/10/28 0028下午 7:24
 */
@Slf4j
public class UserMapperTest extends SpringBootDemoMybatisApplicationTest {
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询所有用户
     */
    @Test
    public void selectAllUser() {
        List<User> users = userMapper.selectAllUser();
        Assert.assertTrue(CollectionUtil.isNotEmpty(users));
        log.debug("【users】:{}", users);
    }

    @Test
    public void selectUserById() {
        User user = userMapper.selectUserById(1L);
        Assert.assertNotNull(user);
        log.debug("【user】 :{}",user);
    }

    @Test
    public void saveUser() {
        String salt = IdUtil.fastSimpleUUID();
        User testSave = User.builder().name("testSave").password("123456").createTime(0L).lastUpdateTime(0L).email("123@qq.com").phoneNumber("12345678987").status(1).build();
        User.getEncryptedUser(testSave);
        int i = userMapper.saveUser(testSave);
        Assert.assertEquals(1,i);
        log.debug("【i】:{}",i);
    }

    @Test
    public void deleteById() {
        int i = userMapper.deleteById(1L);
        Assert.assertEquals(1,i);
        log.debug("【i】:{}",i);
    }
}

```