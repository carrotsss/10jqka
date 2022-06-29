package com.insigma.exception;

/**
 * @ClassName BusinessException
 * @Description
 * @Author carrots
 * @Date 2022/6/29 16:05
 * @Version 1.0
 */
public class BusinessException extends BaseException {
    public BusinessException(FailureCode failureCode) {
        super(failureCode.getCode(), failureCode.getMsg());
    }
}
