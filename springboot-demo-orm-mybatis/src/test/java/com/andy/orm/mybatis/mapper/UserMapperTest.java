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
