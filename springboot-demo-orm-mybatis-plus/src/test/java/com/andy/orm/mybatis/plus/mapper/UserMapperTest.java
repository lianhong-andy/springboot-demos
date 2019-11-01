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
