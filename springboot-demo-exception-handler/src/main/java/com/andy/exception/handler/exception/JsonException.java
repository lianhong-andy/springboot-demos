package com.andy.exception.handler.exception;

import com.andy.exception.handler.constant.Status;
import lombok.Getter;

/**
 * @author lianhong
 * @description json异常类
 * @date 2019/9/17 0017上午 9:45
 */
@Getter
public class JsonException extends BaseException {
    public JsonException(Status status) {
        super(status);
    }

    public JsonException(Integer code, String message) {
        super(code, message);
    }
}
