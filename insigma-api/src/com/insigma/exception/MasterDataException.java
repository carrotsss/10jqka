package com.insigma.exception;

/**
 * @ClassName MasterDataException
 * @Description
 * @Author carrots
 * @Date 2022/6/16 14:17
 * @Version 1.0
 */
public class MasterDataException extends RuntimeException {
    public MasterDataException(String message) {
        super(message);
    }
}
