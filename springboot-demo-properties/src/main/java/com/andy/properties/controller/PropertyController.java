package com.andy.properties.controller;

import cn.hutool.core.lang.Dict;
import com.andy.properties.porperty.ApplicationProperty;
import com.andy.properties.porperty.DeveloperProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lianhong
 * @description
 * @date 2019/9/10 0010下午 1:27
 */
@RestController
public class PropertyController {
    private final ApplicationProperty applicationProperty;
    private final DeveloperProperty developerProperty;

    @Autowired
    public PropertyController(ApplicationProperty applicationProperty,DeveloperProperty developerProperty) {
        this.applicationProperty = applicationProperty;
        this.developerProperty = developerProperty;
    }

    @GetMapping("/property")
    public Dict index() {
        return Dict.create().set("applicationProperty",applicationProperty).set("developProperty",developerProperty);
    }

}
