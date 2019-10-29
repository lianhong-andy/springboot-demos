# springboot-demo-orm-mybatis-mapper-page

> 此 demo 演示了 Spring Boot 如何集成通用Mapper插件和分页助手插件，简化Mybatis开发，带给你难以置信的开发体验。


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

    <artifactId>springboot-demo-orm-mybatis-mapper-page</artifactId>
    <packaging>jar</packaging>
    <version>1.0.0-SNAPSHOT</version>
    <name>springboot-demo-orm-mybatis-mapper-page</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <mybatis.version>1.3.2</mybatis.version>
        <com.andy.orm.mybatis.MapperAndPage.mapper.version>2.0.4</com.andy.orm.mybatis.MapperAndPage.mapper.version>
        <mybatis.pagehelper.version>1.2.9</mybatis.pagehelper.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- 通用Mapper -->
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper-spring-boot-starter</artifactId>
            <version>${com.andy.orm.mybatis.MapperAndPage.mapper.version}</version>
        </dependency>

        <!-- 分页助手 -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>${mybatis.pagehelper.version}</version>
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
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>


</project>
```

## SpringBootDemoOrmMybatisApplication.java
```java
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

```

## application.yml
```yaml
server:
  port: 8094
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
    com.andy.orm.mybatis.MapperAndPage.mapper: trace
mybatis:
  configuration:
    # 下划线转驼峰
    map-underscore-to-camel-case: true
  mapper-locations: classpath:mappers/*.xml
  type-aliases-package: com.andy.orm.mybatis.MapperAndPage.entity
mapper:
  mappers:
  - tk.mybatis.mapper.common.Mapper
  not-empty: true
  style: camelhump
  wrap-keyword: "`{0}`"
  safe-delete: true
  safe-update: true
  identity: MYSQL
pagehelper:
  auto-dialect: true
  helper-dialect: mysql
  reasonable: true
  params: count=countSql

```

## UserMapper.java
```java
package com.andy.orm.mybatis.MapperAndPage.mapper;

import com.andy.orm.mybatis.MapperAndPage.entity.User;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Component
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {
}

```

## UserMapperTest.java
```java
package com.andy.orm.mybatis.MapperAndPage.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.andy.orm.mybatis.MapperAndPage.SpringBootDemoMybatisApplicationTest;
import com.andy.orm.mybatis.MapperAndPage.entity.User;
import com.andy.orm.mybatis.MapperAndPage.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lianhong
 * @description
 * @date 2019/10/28 0028下午 7:24
 */
@Slf4j
public class UserMapperTest extends SpringBootDemoMybatisApplicationTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testQueryAll() {
        List<User> users = userMapper.selectAll();
        Assert.assertTrue(CollectionUtil.isNotEmpty(users));
        log.debug("【users】:{}",users);
    }

    @Test
    public void testQueryById() {
        User user = userMapper.selectByPrimaryKey(1L);
        Assert.assertNotNull(user);
        log.debug("【user】= {}", user);
    }

    @Test
    public void testInsert() {
        User user = User.builder().name("user_3").password("123456").status(1).email("123@qq.com").phoneNumber("12345678932")
                .createTime(DateUtil.currentSeconds()).lastLoginTime(DateUtil.currentSeconds()).lastUpdateTime(DateUtil.currentSeconds()).build();
        User.getEncryptedUser(user);
        int insert = userMapper.insert(user);
        Assert.assertEquals(1,insert);
        log.debug("【insert】:{}",insert);
    }

    /**
     * 测试通用Mapper - 批量保存
     */
    @Test
    public void testBatchInsert() {
        List<User> users = Lists.newArrayList();
        for (int i = 4; i < 14; i++) {
            String salt = IdUtil.fastSimpleUUID();
            long now = DateUtil.currentSeconds();
            User user = User.builder().name("testSave" + i).password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave" + i + "@xkcoding.com").phoneNumber("1730000000" + i).status(1).lastLoginTime(now).createTime(now).lastUpdateTime(now).build();
            users.add(user);
        }
        int i = userMapper.insertList(users);
        Assert.assertEquals(users.size(),i);
        List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
        log.debug("【ids】:{}",ids);
    }

    @Test
    public void testDelete() {
        int i = userMapper.deleteByPrimaryKey(1L);
        Assert.assertEquals(1,i);
        log.debug("【i】:{}",i);
        User user = userMapper.selectByPrimaryKey(1L);
        Assert.assertNull(user);
    }

    /**
     * 测试通用Mapper - 更新
     */
    @Test
    public void testUpt() {
        Long primaryKey = 1L;
        String uptName = "通用Mapper名字更新";
        User user = userMapper.selectByPrimaryKey(primaryKey);
        user.setName("通用Mapper名字更新");
        int i = userMapper.updateByPrimaryKey(user);
        Assert.assertEquals(1,i);
        User uptUser = userMapper.selectByPrimaryKey(primaryKey);
        Assert.assertNotNull(uptUser);
        Assert.assertEquals(uptName,uptUser.getName());
        log.debug("【uptUser】：{}",uptUser);

    }

    /**
     * 测试分页助手 - 分页排序查询
     */
    @Test
    public void testPageQuery() {
        int currentPage = 1;
        int pageSize = 5;
        String orderBy = "id desc";
        int count = userMapper.selectCount(null);
        PageHelper.startPage(currentPage,pageSize,orderBy);
        List<User> users = userMapper.selectAll();
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        Assert.assertEquals(5,userPageInfo.getSize());
        Assert.assertEquals(count,userPageInfo.getTotal());
        log.debug("【userPageInfo】：{}",userPageInfo);
    }

    /**
     * 测试通用Mapper - 条件查询
     */
    @Test
    public void testQueryByCondition() {
        Example example = new Example(User.class);
        //过滤
        example.createCriteria().andLike("name","%Save1%").orEqualTo("phoneNumber","17300000001");
        //排序
        example.setOrderByClause("id desc");
        int count = userMapper.selectCountByExample(example);
        //分页
        PageHelper.startPage(1,3);
        //查询
        List<User> users = userMapper.selectByExample(example);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        Assert.assertEquals(3,users.size());
        Assert.assertEquals(count,pageInfo.getTotal());
        log.debug("【userPageInfo】：{}",pageInfo);

    }

    /**
     * 初始化数据
     */
    @Before
    public void init() {
        testBatchInsert();
    }

}

```


## 参考

- 通用Mapper官方文档：https://github.com/abel533/Mapper/wiki/1.integration
- pagehelper 官方