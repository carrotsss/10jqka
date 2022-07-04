package com.insigma.handler.quote.command;

import lombok.Data;

/**
 * @ClassName CommandWrapper
 * @Description
 * @Author carrots
 * @Date 2022/6/23 16:10
 * @Version 1.0
 */
@Data
public class CommandWrapper {
    private String commandType;
    private byte[] key;
    private byte[] field;
    private byte[] value;
}
