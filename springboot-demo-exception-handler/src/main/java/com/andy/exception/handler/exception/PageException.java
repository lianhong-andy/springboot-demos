package com.andy.exception.handler.exception;

import com.andy.exception.handler.constant.Status;
import lombok.Getter;

/**
 * @author lianhong
 * @description 页面异常类
 * @date 2019/9/17 0017上午 9:46
 */
@Getter
public class PageException extends BaseException {
    public PageException(Status status) {
        super(status);
    }

    public PageException(Integer code, String message) {
        super(code, message);
    }
}
