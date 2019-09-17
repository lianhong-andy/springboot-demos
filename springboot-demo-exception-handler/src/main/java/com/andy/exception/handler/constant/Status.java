package com.andy.exception.handler.constant;

import lombok.Getter;

/**
 * @author lianhong
 * @description 状态码封装
 * @date 2019/9/17 0017上午 9:00
 */
@Getter
public enum  Status {
    /**
     * 操作成功
     */
    OK(200, "操作成功"),

    /**
     * 未知异常
     */
    UNKNOW_EXCEPTION(500,"服务器出错啦");


    /**
     * 状态码
     */
    private Integer code;
    /**
     * 内容
     */
    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
