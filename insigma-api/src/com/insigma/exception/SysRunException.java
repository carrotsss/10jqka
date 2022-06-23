package com.insigma.exception;

/**
 * @ClassName SysRunException
 * @Description
 * @Author carrots
 * @Date 2022/6/16 14:37
 * @Version 1.0
 */
public class SysRunException extends RuntimeException{
    public SysRunException(String message) {
        super(message);
    }

    public SysRunException(String message, Throwable cause) {
        super(message, cause);
    }

    public SysRunException(Throwable casse) {
        super(casse);
    }
}
