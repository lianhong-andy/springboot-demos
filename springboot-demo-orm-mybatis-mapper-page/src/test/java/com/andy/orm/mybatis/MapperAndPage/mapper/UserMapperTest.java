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
