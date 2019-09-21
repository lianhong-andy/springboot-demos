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
