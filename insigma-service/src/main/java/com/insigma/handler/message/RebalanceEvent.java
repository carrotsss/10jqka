package com.insigma.handler.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName RebalanceEvent
 * @Description
 * @Author carrots
 * @Date 2022/6/27 14:24
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RebalanceEvent {
    private String topic;
}
