# springboot-demo-orm-mybatis-plus

> 此 demo 演示了 Spring Boot 如何集成 mybatis-plus，简化Mybatis开发，带给你难以置信的开发体验。

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

    <artifactId>springboot-demo-orm-mybatis-plus</artifactId>
    <packaging>jar</packaging>
    <version>1.0.0-SNAPSHOT</version>
    <name>springboot-demo-orm-mybatis-plus</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <mybatis.plus.version>3.1.0</mybatis.plus.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis.plus.version}</version>
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
        <finalName>springboot-demo-orm-mybatis-plus</finalName>
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
## 启动类
```java
package com.andy.orm.mybatis.plus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029下午 8:52
 */
@SpringBootApplication
public class SpringBootDemoOrmMybatisPlusApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoOrmMybatisPlusApplication.class,args);
    }
}

```

## 实体类
```java
package com.andy.orm.mybatis.plus.entity;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.andy.orm.mybatis.plus.constant.Const;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029下午 8:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("orm_user")
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
     * 创建时间(新增时自动填充)
     */
    @TableField(fill = INSERT)
    private Date createTime;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 上次更新时间(更新时自动填充)
     */
    @TableField(fill = INSERT_UPDATE)
    private Date lastUpdateTime;

    public static void getEncryptedUser(User user) {
        String salt = IdUtil.simpleUUID();
        String pwd = SecureUtil.md5(user.getPassword() + Const.SALT_PREFIX + salt);
        user.setPassword(pwd);
        user.setSalt(salt);
    }
}


```
## config
- com.andy.orm.mybatis.plus.config.CommonFieldHandler
```java
package com.andy.orm.mybatis.plus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029下午 9:29
 */
@Slf4j
@Component
public class CommonFieldHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("lastUpdateTime",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("lastUpdateTime", new Date(),metaObject);
    }
}

```
- com.andy.orm.mybatis.plus.config.MybatisPlusConfig
```java
package com.andy.orm.mybatis.plus.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029下午 9:26
 */
@Configuration
@MapperScan(basePackages = {"ccom.andy.orm.mybatis.plus.mapper"})
@EnableTransactionManagement
public class MybatisPlusConfig {
    /**
     * 性能分析拦截器，不建议生产使用
     * @return
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}

```

## mappper
```java
package com.andy.orm.mybatis.plus.mapper;

import com.andy.orm.mybatis.plus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

```

## service
```java
public interface aUserService extends IService<User> {
}
```

## service.impl
```java
package com.andy.orm.mybatis.plus.service.impl;

import com.andy.orm.mybatis.plus.entity.User;
import com.andy.orm.mybatis.plus.mapper.UserMapper;
import com.andy.orm.mybatis.plus.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029下午 9:10
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}

```
## com.andy.orm.mybatis.plus.mapper.UserMapperTest
```java
package com.andy.orm.mybatis.plus.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.andy.orm.mybatis.plus.SpringBootDemoOrmMybatisPlusApplicationTest;
import com.andy.orm.mybatis.plus.entity.User;
import com.andy.orm.mybatis.plus.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029下午 9:15
 */
@Slf4j
public class UserMapperTest extends SpringBootDemoOrmMybatisPlusApplicationTest {
    @Autowired
    private UserService userService;

    @Test
    public void testQueryAll() {
        List<User> list = userService.list();
        Assert.assertTrue(CollectionUtil.isNotEmpty(list));
        log.debug("【list】:{}", list);
    }

    @Test
    public void testQueryById() {
        User user = userService.getById(1L);
        Assert.assertNotNull(user);
        log.debug("【user】= {}", user);
    }

    @Test
    public void testInsert() {
        User user = User.builder().name("user_3").password("123456").status(1).email("123@qq.com").phoneNumber("12345678932")
                .createTime(new DateTime()).lastLoginTime(new DateTime()).lastUpdateTime(new DateTime()).build();
        User.getEncryptedUser(user);
        boolean save = userService.save(user);
        Assert.assertTrue(save);
        log.debug("【save】:{}", save);
    }

    /**
     * 测试通用Mapper -批量保存
     */
    @Test

    public void testBatchInsert() {
        List<User> users = Lists.newArrayList();
        for (int i = 4; i < 14; i++) {
            String salt = IdUtil.fastSimpleUUID();
            DateTime now = new DateTime();
            User user = User.builder().name("testSave" + i).password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave" + i + "@xkcoding.com").phoneNumber("1730000000" + i).status(1).lastLoginTime(now).createTime(now).lastUpdateTime(now).build();
            users.add(user);
        }
        boolean b = userService.saveBatch(users);
        Assert.assertTrue(b);
        List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
        log.debug("【ids】:{}", ids);
    }

    @Test
    public void testDelete() {
        boolean b = userService.removeById(1L);
        Assert.assertTrue(b);
        log.debug("【b】:{}", b);
        User user = userService.getById(1L);
        Assert.assertNull(user);
    }

    /**
     * 测试通用Mapper -更新
     */
    @Test

    public void testUpt() {
        User user = userService.getById(1L);
        Assert.assertNotNull(user);
        user.setName("MybatisPlus修改名字");
        boolean b = userService.updateById(user);
        Assert.assertTrue(b);
        User update = userService.getById(1L);
        Assert.assertEquals("MybatisPlus修改名字", update.getName());
        log.debug("【update】= {}", update);

    }

    /**
     * 测试分页助手 - 分页排序查询
     */
    @Test
    public void testPageQuery() {
        int currentPage = 1;
        int pageSize = 5;
        String orderBy = "id desc";
        Page<User> page = new Page<>(currentPage, pageSize);
        page.setDesc(orderBy);
        int count = userService.count(new QueryWrapper<>());
        IPage<User> userPageInfo = userService.page(page, new QueryWrapper<>());
        Assert.assertEquals(5, userPageInfo.getSize());
        Assert.assertEquals(count, userPageInfo.getTotal());
        log.debug("【userPageInfo】：{}", userPageInfo);
    }

    /**
     * 测试通用Mapper - 条件查询
     */
    @Test
    public void testQueryByCondition() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //过滤
        wrapper.like("name", "Save1").or().eq("phone_number", "17300000001").orderByDesc("id");
        //排序
        int count = userService.count(wrapper);
        //分页
        Page<User> page = new Page<>(1,3);
        //查询
        IPage<User> pageInfo = userService.page(page, wrapper);
        Assert.assertEquals(3, pageInfo.getSize());
        Assert.assertEquals(count, pageInfo.getTotal());
        log.debug("【userPageInfo】：{}", pageInfo);

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


## 在mapper上使用@Component时报如下错误：
```text
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.andy.orm.mybatis.plus.mapper.UserMapper' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.raiseNoMatchingBeanFound(DefaultListableBeanFactory.java:1658)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1217)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1171)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:593)
	... 43 more

```

## 加上@Mapper之后就不会了