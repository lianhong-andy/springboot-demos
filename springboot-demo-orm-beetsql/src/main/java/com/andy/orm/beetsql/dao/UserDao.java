package com.andy.orm.beetsql.dao;

import com.andy.orm.beetsql.entity.User;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface UserDao extends BaseMapper<User> {
}
