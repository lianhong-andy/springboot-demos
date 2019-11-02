# sprinboot-demo-orm-beetlsql

> 此 demo 主要演示了 Spring Boot 如何整合 beetl sql 快捷操作数据库，使用的是beetl官方提供的beetl-framework-starter集成。集成过程不是十分顺利，没有其他的orm框架集成的便捷。

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

    <artifactId>springboot-demo-orm-beetsql</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>springboot-demo-orm-beetsql</name>
    <description>Demo project for Spring Boot</description>


    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <ibeetl.version>1.1.68.RELEASE</ibeetl.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <dependency>
            <groupId>com.ibeetl</groupId>
            <artifactId>beetl-framework-starter</artifactId>
            <version>${ibeetl.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
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
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <finalName>springboot-demo-orm-beetlsql</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>



</project>
```

## application.yml 

> 注意下方注释的地方，**不能解开注释，并且需要通过JavaConfig的方式手动配置数据源**，否则，会导致beetl启动失败，因此，初始化数据库的数据，只能手动在数据库使用 resources/db 下的建表语句和数据库初始化数据。
```yaml
spring:
  datasource:
    url: jdbc:mysql://49.234.41.101:33306/sbt?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8
    username: root
    password: 1234Andy
    driver-class-name: com.mysql.cj.jdbc.Driver
#### beetlsql starter不能开启下面选项
#    type: com.zaxxer.hikari.HikariDataSource
#    initialization-mode: always
#    continue-on-error: true
#    schema:
#    - "classpath:db/schema.sql"
#    data:
#    - "classpath:db/data.sql"
#    hikari:
#      minimum-idle: 5
#      connection-test-query: SELECT 1 FROM DUAL
#      maximum-pool-size: 20
#      auto-commit: true
#      idle-timeout: 30000
#      pool-name: SpringBootDemoHikariCP
#      max-lifetime: 60000
#      connection-timeout: 30000
logging:
  level:
    com.andy: debug
    com.andy.orm.beetsql: trace
beetl:
  enabled: false
beetlsql:
  enabled: true
  sqlPath: /sql
  daoSuffix: Dao
  basePackage: com.andy.orm.beetsql.dao
  dbStyle: org.beetl.sql.core.db.MySqlStyle
  nameConversion: org.beetl.sql.core.UnderlinedNameConversion
beet-beetlsql:
  dev: true
```

## BeetlConfig.java
```java
package com.andy.orm.beetsql.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author lianhong
 * @description
 * @date 2019/11/1 0001下午 7:49
 */
@Configuration
public class BeetConfig {
    /**
     * Beetl需要显示的配置数据源，方可启动项目，大坑，切记！
     * @param env
     * @return
     */
    @Bean(name = "datasource")
    public DataSource getDataSource(Environment env) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;

    }
}

```
## UserDao.java
```java
package com.andy.orm.beetsql.dao;

import com.andy.orm.beetsql.entity.User;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface UserDao extends BaseMapper<User> {
}

```

## entity
```java
package com.andy.orm.beetsql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lianhong
 * @description
 * @date 2019/11/1 0001下午 7:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orm_user")
public class User implements Serializable {
    private static final long serialVersionUID = -1840831686851699943L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 加密使用的盐
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 状态，-1：逻辑删除，0：禁用，1：启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;
}

```

## service
```java
package com.andy.orm.beetsql.service;

import com.andy.orm.beetsql.entity.User;
import org.beetl.sql.core.engine.PageQuery;

import java.util.List;

public interface UserService {
    /**
     * 新增用户
     *
     * @param user 用户
     * @return 保存的用户
     */
    User saveUser(User user);


    /**
     * 批量插入用户
     *
     * @param users 用户列表
     */
    void saveUserList(List<User> users);

    /**
     * 根据主键删除用户
     *
     * @param id 主键
     */
    void deleteUser(Long id);

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 更新后的用户
     */
    User updateUser(User user);

    /**
     * 查询单个用户
     *
     * @param id 主键id
     * @return 用户信息
     */
    User getUser(Long id);

    /**
     * 查询用户列表
     *
     * @return 用户列表
     */
    List<User> getUserList();

    /**
     * 分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    每页条数
     * @return 分页用户列表
     */
    PageQuery<User> getUserByPage(Integer currentPage, Integer pageSize);
}

```
## serviceImpl
```java
package com.andy.orm.beetsql.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.andy.orm.beetsql.dao.UserDao;
import com.andy.orm.beetsql.entity.User;
import com.andy.orm.beetsql.service.UserService;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lianhong
 * @description
 * @date 2019/11/1 0001下午 7:55
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public User saveUser(User user) {
        userDao.insert(user,true);
        return user;
    }

    /**
     * 批量插入用户
     *
     * @param users 用户列表
     */
    @Override
    public void saveUserList(List<User> users) {
        userDao.insertBatch(users);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }


    /**
     * 更新用户
     *
     * @param user 用户
     * @return 更新后的用户
     */
    @Override
    public User updateUser(User user) {
        if (ObjectUtil.isNull(user)) {
            throw new RuntimeException("用户id不能为null");
        }
        userDao.updateById(user);
        return userDao.single(user.getId());
    }

    /**
     * 查询单个用户
     *
     * @param id 主键id
     * @return 用户信息
     */
    @Override
    public User getUser(Long id) {
        return userDao.single(id);
    }

    /**
     * 查询用户列表
     *
     * @return 用户列表
     */
    @Override
    public List<User> getUserList() {
        return userDao.all();
    }

    /**
     * 分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    每页条数
     * @return 分页用户列表
     */
    @Override
    public PageQuery<User> getUserByPage(Integer currentPage, Integer pageSize) {
        return userDao.createLambdaQuery().page(currentPage,pageSize);
    }
}

```
