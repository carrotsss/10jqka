package com.insigma.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    MONTHLY("monthly", 1, "fund_holding_monthly"),
    EMPTY(null, 0, null),;
    private String type;
    private Integer code;
    private String tablename;

    public static final int COMMON_REPORT_DAY_START_INDEX = 5;

    public static DiscountEnum typeOf(String type) {
        for (DiscountEnum value : DiscountEnum.values()) {
            if (value.type.equalsIgnoreCase(type)) {
                return value;
            }
        }
        return DiscountEnum.DAILY;
    }

    public static DiscountEnum getByNext(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        List<DiscountEnum> discountEnumList = Arrays.stream(DiscountEnum.values())
                .sorted(Comparator.comparing(DiscountEnum::getType))
                .collect(Collectors.toList());
        for (int i = 0; i < discountEnumList.size(); i++) {
            if (!discountEnumList.get(i).getType().equals(type)) {
                continue;
            }
            if (i == discountEnumList.size() - 1) {
                return discountEnumList.get(0);
            } else {
                return discountEnumList.get(i + 1);
            }
        }
    }

    public static DiscountEnum parse(Integer code) {
        if (code == null) {
            return EMPTY;
        }
        for (DiscountEnum discountEnum : values()) {
            if (discountEnum.code.equals(code)) {
                return discountEnum;
            }
        }
        return EMPTY;
    }
}
