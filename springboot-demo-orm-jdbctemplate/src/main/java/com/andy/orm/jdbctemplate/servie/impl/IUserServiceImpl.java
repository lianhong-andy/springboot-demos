package com.andy.orm.jdbctemplate.servie.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.andy.orm.jdbctemplate.dao.UserDao;
import com.andy.orm.jdbctemplate.entity.User;
import com.andy.orm.jdbctemplate.servie.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lianhong
 * @description
 * @date 2019/9/20 0020上午 9:16
 */
@Repository
public class IUserServiceImpl implements IUserService {
    private final UserDao userDao;

    @Autowired
    public IUserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public Boolean save(User user) {
       User.getEncryptedUser(user);
        return userDao.insert(user) > 0;
    }

    @Override
    public Boolean delete(Long id) {
        return userDao.delete(id) > 0;
    }

    @Override
    public Boolean update(User user, Long id) {
        User orgUser = getUser(id);
        if (ObjectUtil.isNotNull(orgUser)) {
            if (StrUtil.isNotBlank(user.getPassword())) {
                User.getEncryptedUser(user);
            }
        }
        BeanUtil.copyProperties(user,orgUser,CopyOptions.create().setIgnoreNullValue(true));
        orgUser.setLastUpdateTime(new DateTime());

        return userDao.update(orgUser,id) > 0;
    }

    @Override
    public User getUser(Long id) {
        return userDao.selectById(id);
    }

    @Override
    public List<User> getUsers(User user) {
        return userDao.selectUserList(user);
    }
}
