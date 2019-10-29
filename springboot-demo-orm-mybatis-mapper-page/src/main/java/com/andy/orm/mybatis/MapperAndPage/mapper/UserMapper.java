package com.andy.orm.mybatis.MapperAndPage.mapper;

import com.andy.orm.mybatis.MapperAndPage.entity.User;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Component
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {
}
