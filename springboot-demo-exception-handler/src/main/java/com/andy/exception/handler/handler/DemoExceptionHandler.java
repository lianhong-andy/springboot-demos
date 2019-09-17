package com.andy.exception.handler.handler;

import com.andy.exception.handler.exception.JsonException;
import com.andy.exception.handler.exception.PageException;
import com.andy.exception.handler.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lianhong
 * @description 统一异常处理器
 * @date 2019/9/17 0017上午 9:47
 */
@ControllerAdvice
@Slf4j
public class DemoExceptionHandler {
    private static final String DEFAULT_ERROR_VIEW = "error";
    private static final String MESSAGE = "message";

    /**
     * 统一 json 异常处理
     *
     * @param exception JsonException
     * @return 统一返回json格式
     */
    @ExceptionHandler(value = JsonException.class)
    @ResponseBody
    public ApiResponse jsonErrorHandler(JsonException exception) {
        log.error("【JsonException】：{}", exception.getMessage());
        return ApiResponse.ofException(exception);
    }

    @ExceptionHandler(value = PageException.class)
    @ResponseBody
    public ModelAndView pageErrorHandler(PageException exception) {
        log.error("【DemoPageException】：{}", exception.getMessage());
        ModelAndView view = new ModelAndView();
        view.addObject(MESSAGE, exception.getMessage());
        view.setViewName(DEFAULT_ERROR_VIEW);
        return view;
    }
}
