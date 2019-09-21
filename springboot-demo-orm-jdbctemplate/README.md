# SpringBoot-demo-orm-jdbctemplate
> 本 demo 主要演示了Spring Boot如何使用 JdbcTemplate 操作数据库，并且简易地封装了一个通用的 Dao 层，包括增删改查。

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

    <artifactId>springboot-demo-orm-jdbctemplate</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>springboot-demo-template-thymeleaf</name>
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
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
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

        <!-- 热部署 -->
        <dependency>
            <groupId> org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <finalName>springboot-demo-orm-jdbctemplate</finalName>
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

## BaseDao.java
```java
package com.andy.orm.jdbctemplate.dao.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.andy.orm.jdbctemplate.constant.Const;
import com.andy.orm.jdbctemplate.annotation.Column;
import com.andy.orm.jdbctemplate.annotation.PK;
import com.andy.orm.jdbctemplate.annotation.Table;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lianhong
 * @description Dao基类
 * @date 2019/9/19 0019下午 1:32
 */
@Slf4j
public class BaseDao<T, P> {
    private JdbcTemplate jdbcTemplate;
    private Class<T> clzz;

    @SuppressWarnings(value = "uncheck")
    public BaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.clzz = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Integer insert(T t, Boolean ignoreNull) {
        String table = getTableName(t);

        List<Field> filterField = getField(t, ignoreNull);

        List<String> columnList = getColumns(filterField);

        String columns = StrUtil.join(Const.SEPARATOR_COMMA, columnList);

        //构造占位符
        String params = StrUtil.repeatAndJoin("?", columnList.size(), Const.SEPARATOR_COMMA);

        //构造值
        Object[] values = filterField.stream().map(field -> ReflectUtil.getFieldValue(t, field)).toArray();

        String sql = StrUtil.format("INSERT INTO {table} ({columns}) VALUES ({params})"
                , Dict.create().set("table", table).set("columns", columnList).set("params", params));

        log.debug("【执行sql】sql:{}", sql);
        log.debug("【执行sql】params:{}", JSONUtil.toJsonStr(values));

        return jdbcTemplate.update(sql, values);
    }


    /**
     * 获取表名
     *
     * @param t 对象
     * @return 表名
     */
    private String getTableName(T t) {
        Table tableAnnotation = t.getClass().getAnnotation(Table.class);
        if (ObjectUtil.isNotNull(tableAnnotation)) {
            return StrUtil.format("`{}`", tableAnnotation.name());
        } else {
            return StrUtil.format("`{}`", t.getClass().getName().toLowerCase());
        }

    }

    /**
     * 获取表名
     *
     * @return 表名
     */
    private String getTableName() {
        Table tableAnnotation = clzz.getAnnotation(Table.class);
        if (ObjectUtil.isNotNull(tableAnnotation)) {
            return StrUtil.format("`{}`", tableAnnotation.name());
        } else {
            return StrUtil.format("`{}`", clzz.getName().toLowerCase());
        }
    }

    /**
     * 获取字段列表 {@code 过滤数据库中不存在的字段，以及自增列}
     *
     * @param t
     * @param ignoreNull
     * @return
     */
    private List<Field> getField(T t, Boolean ignoreNull) {
        //获取所有字段，包含父类中的字段
        Field[] fields = ReflectUtil.getFields(t.getClass());

        //过滤数据库中不存在的字段，以及自增列
        List<Field> filterField;
        Stream<Field> fieldStream = CollUtil.toList(fields).stream().filter(field -> ObjectUtil.isNull(field.getAnnotation(Ignore.class))
                || ObjectUtil.isNull(field.getAnnotation(PK.class)));

        if (ignoreNull) {
            filterField = fieldStream.filter(field -> ObjectUtil.isNotNull(ReflectUtil.getFieldValue(t, field))).collect(Collectors.toList());
        } else {
            filterField = fieldStream.collect(Collectors.toList());
        }
        return filterField;
    }

    /**
     * 获取列
     *
     * @param fieldList 字段列表
     * @return 列信息列表
     */
    private List<String> getColumns(List<Field> fieldList) {
        List<String> columnList = CollUtil.newArrayList();
        for (Field field : fieldList) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            String columnName;
            if (ObjectUtil.isNotNull(columnAnnotation)) {
                columnName = columnAnnotation.name();
            } else {
                columnName = field.getName();
            }

            columnList.add(StrUtil.format("`{}`", columnName));
        }
        return columnList;
    }

    /**
     * 通用根据主键删除
     *
     * @param pk 主键
     * @return 影响行数
     */
    protected Integer deleteById(P pk) {
        String tableName = getTableName();
        String sql = StrUtil.format("DELETE FROM {table} where id = ?", Dict.create().set("table", tableName));
        log.debug("【执行sql】sql:{}", sql);
        log.debug("【执行sql】params:{}", JSONUtil.toJsonStr(pk));
        return jdbcTemplate.update(sql, pk);
    }


    /**
     * 通用根据主键更新，自增列需要添加{@link PK}注解
     *
     * @param t          对象
     * @param pk         主键
     * @param ignoreNull 是否忽略null值
     * @return
     */
    protected Integer updateById(T t, P pk, Boolean ignoreNull) {
        String tableName = getTableName(t);

        List<Field> filterFieldList = getField(t, ignoreNull);

        List<String> columnList = getColumns(filterFieldList);

        List<String> columns = columnList.stream().map(s -> StrUtil.appendIfMissing(s, "=?")).collect(Collectors.toList());
        String params = StrUtil.join(Const.SEPARATOR_COMMA, columns);

        //构造值
        List<Object> valueList = filterFieldList.stream().map(field -> ReflectUtil.getFieldValue(t, field)).collect(Collectors.toList());
        valueList.add(pk);

        Object[] values = ArrayUtil.toArray(valueList, Object.class);

        String sql = StrUtil.format("UPDATE {table} SET {params} WHERE id = ?", Dict.create().set("table", tableName).set("params", params));

        log.debug("【执行SQL】SQL：{}", sql);
        log.debug("【执行SQL】参数：{}", JSONUtil.toJsonStr(values));
        return jdbcTemplate.update(sql, values);
    }


    /**
     * 通用根据主键查找单条记录
     *
     * @param pk 主键
     * @return 单条记录
     */
    protected T findOneById(P pk) {
        String tableName = getTableName();

        String sql = StrUtil.format("SELECT * FROM {table} where id = ?", Dict.create().set("table", tableName));

        log.debug("【执行SQL】SQL：{}", sql);
        log.debug("【执行SQL】参数：{}", JSONUtil.toJsonStr(pk));
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(clzz),pk);
    }

    protected List<T> findByExample(T t) {
        String tableName = getTableName(t);

        List<Field> filterFieldList = getField(t, true);

        List<String> columnList = getColumns(filterFieldList);

        List<String> columns = columnList.stream().map(s -> " and " + s + "=?").collect(Collectors.toList());

        String where = StrUtil.join(Const.SEPARATOR_BLANK, columns);
        //构造值
        Object[] values = filterFieldList.stream().map(field -> ReflectUtil.getFieldValue(t, field)).toArray();

        String sql = StrUtil.format("SELECT * FROM {table} where 1 = 1 {where}", Dict.create().set("table", tableName).set("where", where));

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql,  values);
        List<T> ret = CollUtil.newArrayList();
        maps.forEach(map -> ret.add(BeanUtil.fillBeanWithMap(map,ReflectUtil.newInstance(clzz),true,false)));

        return ret;
    }

}

```

## 设计注解
### com.andy.orm.jdbctemplate.annotation.Column 字段注解
```java
package com.andy.orm.jdbctemplate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 所有阶段都保留
 */
@Retention(RetentionPolicy.RUNTIME)
/**作用在字段上*/
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 列名
     *
     * @return 列名
     */
    String name();
}

```
### com.andy.orm.jdbctemplate.annotation.Ignore 需要忽略的字段
```java
package com.andy.orm.jdbctemplate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要忽略的字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Ignore {
}

```
### com.andy.orm.jdbctemplate.annotation.PK 主键注解
```java
package com.andy.orm.jdbctemplate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PK {
    /**
     * 自增
     *
     * @return 自增主键
     */
    boolean auto() default true;
}

```
### com.andy.orm.jdbctemplate.annotation.Table 表名注解
```java
package com.andy.orm.jdbctemplate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 表名
     * @return 表名
     */
    String name();
}

```

## application.yml
```yaml
server:
  port: 8091
  servlet:
    context-path: /demo
spring:
  datasource:
    url: jdbc:mysql://49.234.41.101:33306/sbt?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8
    username: root
    password: 1234Andy
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
      connection-test-query: SELECT 1 FROM DUAL
      maximum-pool-size: 20
      auto-commit: true
      idle-timeout: 3000
      pool-name: SpringBootDemoHikariCP
      max-lifetime: 60000
      connection-timeout: 30000
logging:
  level:
    com.andy: debug

```

## 注意点
> 1. 实体类必须实现Serializable
> 2. 理解Dao层的封装思想，用于实际开发中
> 3. 理解注解的使用
> 4. 理解orm的映射思想

## SpringBoot集成MybatisPlus, Redis,hikari ,swagger2 带配置写法参考链接
 https://blog.csdn.net/qq_24232123/article/details/84388123