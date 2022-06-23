package com.insigma.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName IfindIdReqDTO
 * @Description
 * @Author carrots
 * @Date 2022/6/23 13:18
 * @Version 1.0
 */
@Data
public class IfindIdReqDTO implements Serializable {
    @NotEmpty(message = "param ifindId can not be empty")
    private String ifindId;
    private Integer TradePeriodType;
}
