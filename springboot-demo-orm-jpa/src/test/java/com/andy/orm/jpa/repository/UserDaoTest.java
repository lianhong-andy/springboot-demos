package com.andy.orm.jpa.repository;

import cn.hutool.core.date.DateTime;
import com.andy.orm.jpa.SpringBootDemoJpaApplicationTests;
import com.andy.orm.jpa.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lianhong
 * @description
 * @date 2019/9/22 0022下午 1:07
 */
@Slf4j
public class UserDaoTest extends SpringBootDemoJpaApplicationTests {

    @Autowired
    private UserDao userDao;

    /**
     * 测试保存
     */
    @Test
    public void testSave(){
        User user = User.builder().email("123@qq.com").name("andy").password("123456").status(1).phoneNumber("18924229998").lastLoginTime(new DateTime()).build();
        User.getEncryptedUser(user);
        userDao.save(user);
        Assert.assertNotNull(user.getId());

        Optional<User> byId = userDao.findById(user.getId());
        Assert.assertTrue(byId.isPresent());
        log.debug("byId = {}" ,byId.get());


    }

    /**
     * 测试删除
     */
    @Test
    public void testDel() {
        long count = userDao.count();
        userDao.deleteById(count);
        long leftCount = userDao.count();
        Assert.assertEquals(leftCount,count-1);
    }

    /**
     * 测试修改
     */
    @Test
    public void testUpt() {
        long count = userDao.count();
        userDao.findById(count).ifPresent(user -> {
            user.setName("JPA修改用户名");
            userDao.save(user);
        });
        Assert.assertEquals("JPA修改用户名",userDao.findById(count).get().getName());
    }

    /**
     * 测试查询单个
     */
    @Test
    public void testQueryOne() {
        Optional<User> byId = userDao.findById(1L);
        Assert.assertTrue(byId.isPresent());
        log.debug("【byId】 = {}",byId);
        User user = byId.get();
        System.out.println(user.getEmail());
    }

    /**
     * 测试查询所有
     */
    @Test
    public void testQueryAll() {
        List<User> all = userDao.findAll();
        Assert.assertNotEquals(0,all.size());
        log.debug("size:{},all = {}",all.size(),all);
    }

    /**
     * 测试分页查询
     */
    @Test
    public void testPageQuery() {
        Integer pageNumber = 1;
        Integer pageSize = 5;
        //jpa分页查询是页码数减一
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize,sort);
        Page<User> page = userDao.findAll(pageRequest);
        long totalElements = page.getTotalElements();
        Assert.assertEquals(5,page.getSize());
        Assert.assertEquals(totalElements,userDao.count());
        log.debug("【id】 = {}",page.getContent().stream().map(User::getId).collect(Collectors.toList()));

    }

    /**
     * 初始化10条数据
     */
    @Before
    public void initData() {
        List<User> userList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            User user = User.builder().name("andy"+i).password("123456").email("123"+i+"@qq.com").phoneNumber("1234567890"+i).status(1).lastLoginTime(new DateTime()).build();
            User.getEncryptedUser(user);
            userList.add(user);
        }

        userDao.saveAll(userList);
    }
}
