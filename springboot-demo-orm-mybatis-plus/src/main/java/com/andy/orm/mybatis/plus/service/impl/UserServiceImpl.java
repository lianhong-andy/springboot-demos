package com.andy.orm.mybatis.plus.service.impl;

import com.andy.orm.mybatis.plus.entity.User;
import com.andy.orm.mybatis.plus.mapper.UserMapper;
import com.andy.orm.mybatis.plus.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lianhong
 * @description
 * @date 2019/10/29 0029下午 9:10
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
