package com.insigma.enums;

/**
 * @ClassName ArkFundTableEnum
 * @Description
 * @Author carrots
 * @Date 2022/6/16 13:59
 * @Version 1.0
 */
public enum ArkFundTableEnum {
    USK("funding"),
    EMPTY(null);
    private String name;

    ArkFundTableEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean checkValidTableName(String name) {
        if (name == null) {
            return false;
        }
        for (ArkFundTableEnum value : values()) {
            if (value.getName().equals(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
