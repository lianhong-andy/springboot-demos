package com.andy.template.thymeleaf.controller;

import cn.hutool.core.util.ObjectUtil;
import com.andy.template.thymeleaf.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lianhong
 * @description
 * @date 2019/9/18 0018下午 7:39
 */
@RestController
@Slf4j
public class IndexController {
    @GetMapping(value = {"", "/"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");

        if (ObjectUtil.isNull(user)) {
            mv.setViewName("redirect:/user/login");
        } else {
            mv.addObject(user);
            mv.setViewName("page/index");
        }

        return mv;
    }
}
