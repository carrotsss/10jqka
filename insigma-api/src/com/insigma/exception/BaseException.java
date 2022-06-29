package com.insigma.exception;

/**
 * @ClassName BaseException
 * @Description
 * @Author carrots
 * @Date 2022/6/29 16:00
 * @Version 1.0
 */
public class BaseException extends RuntimeException {
    private final String code;
    private final String msg;

    public BaseException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
