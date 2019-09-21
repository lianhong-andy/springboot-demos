package com.andy.orm.jdbctemplate.controller;

import cn.hutool.core.lang.Dict;
import com.andy.orm.jdbctemplate.entity.User;
import com.andy.orm.jdbctemplate.servie.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lianhong
 * @description
 * @date 2019/9/20 0020下午 1:14
 */
@RestController
@Slf4j
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.userService = iUserService;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping("/user")
    public Dict save(@RequestBody User user) {
        Boolean save = userService.save(user);
        return Dict.create().set("code", save ? 200 : 500).set("msg", save ? "成功" : "失败")
                .set("data", save ? user : null);
    }

    /**
     * 修改用户
     *
     * @param user
     * @param id
     * @return
     */
    @PutMapping("/user/{id}")
    public Dict update(@RequestBody User user, @PathVariable Long id) {
        Boolean save = userService.update(user,user.getId());
        return Dict.create().set("code", save ? 200 : 500).set("msg", save ? "成功" : "失败")
                .set("data", save ? user : null);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/user/{id}")
    public Dict delete(@PathVariable Long id) {
        Boolean save = userService.delete(id);
        return Dict.create().set("code", save ? 200 : 500).set("msg", save ? "成功" : "失败");
    }

    /**
     * 获取用户
     * http://localhost:8091/demo/user/1
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public Dict getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return Dict.create().set("code", 200).set("msg","成功").set("data",user);
    }

    /**
     * 获取用户列表
     * http://localhost:8091/demo/user?status=1
     * @param user
     * @return
     */
    @GetMapping("/user")
    public Dict getUser(User user) {
        List<User> users = userService.getUsers(user);
        return Dict.create().set("code", 200).set("msg","成功").set("data",users);
    }

}
