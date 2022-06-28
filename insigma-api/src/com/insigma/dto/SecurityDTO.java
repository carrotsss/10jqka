package com.insigma.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SecurityDTO
 * @Description
 * @Author carrots
 * @Date 2022/6/27 14:14
 * @Version 1.0
 */
@Data
public class SecurityDTO implements Serializable {
    private String market;
    private String symbol;
}
