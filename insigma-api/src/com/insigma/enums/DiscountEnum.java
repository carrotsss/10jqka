package com.insigma.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName DiscountEnum
 * @Description
 * @Author carrots
 * @Date 2022/6/16 14:06
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DiscountEnum {
    DAILY("daily", 0, "fund_holding_daily"),
    MONTHLY("monthly", 1, "fund_holding_monthly");
    private String type;
    private Integer code;
    private String tablename;

    public static DiscountEnum typeOf(String type) {
        for (DiscountEnum value : DiscountEnum.values()) {
            if (value.type.equalsIgnoreCase(type)) {
                return value;
            }
        }
        return DiscountEnum.DAILY;
    }

    public static DiscountEnum parse(Integer code) {
        if (code == null) {
            return null;
        }
        for (DiscountEnum discountEnum : values()) {
            if (discountEnum.code.equals(code)) {
                return discountEnum;
            }
        }
        return null;
    }
}
