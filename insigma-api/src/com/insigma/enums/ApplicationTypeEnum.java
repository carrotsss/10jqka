package com.insigma.enums;

/**
 * @ClassName ApplicationTypeEnum
 * @Description TODO
 * @Author carrots
 * @Date 2022/6/15 13:19
 * @Version 1.0
 **/
public enum ApplicationTypeEnum {
    TEXT(0);
    private Integer type;

    ApplicationTypeEnum(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static ApplicationTypeEnum getAppListenerType(Integer type) {
        if (type == null) {
            return null;
        }
        for (ApplicationTypeEnum applicationTypeEnum : values()) {
            if (applicationTypeEnum.getType().equals(type)) {
                return applicationTypeEnum;
            }
        }
        return null;
    }

    public static boolean isEquals(ApplicationTypeEnum applicationTypeEnum, String targetType) {
        return applicationTypeEnum.getType().equals(targetType);
    }
}
