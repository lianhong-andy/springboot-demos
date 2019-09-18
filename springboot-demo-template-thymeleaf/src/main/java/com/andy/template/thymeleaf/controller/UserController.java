package com.andy.template.thymeleaf.controller;

import cn.hutool.core.util.ObjectUtil;
import com.andy.template.thymeleaf.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lianhong
 * @description
 * @date 2019/9/18 0018下午 7:39
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request, User user) {
        ModelAndView mv = new ModelAndView();
        if (ObjectUtil.isNull(user)) {
            mv.setViewName("redirect:page/login");
        } else {
            mv.addObject(user);
            mv.setViewName("redirect:/");
        }

        request.getSession().setAttribute("user",user);

        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("page/login");
    }
}
