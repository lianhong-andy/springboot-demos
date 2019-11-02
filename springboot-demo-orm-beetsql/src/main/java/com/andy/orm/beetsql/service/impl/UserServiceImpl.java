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
