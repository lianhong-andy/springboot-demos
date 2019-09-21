package com.andy.orm.jdbctemplate.dao;

import com.andy.orm.jdbctemplate.dao.base.BaseDao;
import com.andy.orm.jdbctemplate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lianhong
 * @description
 * @date 2019/9/19 0019下午 1:31
 */
@Repository
public class UserDao extends BaseDao<User, Long> {

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * 保存用户
     *
     * @param user 用户对象
     * @return 操作影响行数
     */
    public Integer insert(User user) {
        return super.insert(user, true);
    }

    /**
     * 根据主键删除用户
     *
     * @param id 主键id
     * @return 操作影响行数
     */
    public Integer delete(Long id) {
        return super.deleteById(id);
    }

    /**
     * 更新用户
     *
     * @param user 用户对象
     * @param id   主键id
     * @return 操作影响行数
     */
    public Integer update(User user, Long id) {
        return super.updateById(user, id, true);
    }

    /**
     * 根据主键获取用户
     *
     * @param id 主键
     * @return id对应用户
     */
    public User selectById(Long id) {
        return super.findOneById(id);
    }

    /**
     * 获取用户列表
     *
     * @param user 用户实体
     * @return 用户列表
     */
    public List<User> selectUserList(User user) {
        return super.findByExample(user);
    }


}
