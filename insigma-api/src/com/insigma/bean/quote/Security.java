package com.insigma.bean.quote;

import lombok.*;

/**
 * @ClassName Security
 * @Description
 * @Author carrots
 * @Date 2022/6/28 10:22
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Security {
    private String market;
    private String code;
    private String ifindId;
}
