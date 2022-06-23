package main.java.com.insigma.util;

import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;

/**
 * @ClassName ArithmeticUtils
 * @Description
 * @Author carrots
 * @Date 2022/6/20 13:14
 * @Version 1.0
 */
public final class ArithmeticUtils {
    /**
     * 小数位保留位数
     */
    private static final Integer FLOEATE_PRECISION = 10;

    private ArithmeticUtils() {
        //禁止实例化
    }

    /**
     *
     * @param var
     * @return
     */
    public static BigDecimal toBigDecimal(String var) {
        if (NumberUtils.isNumber(var)) {
            return new BigDecimal(var);
        }
        return new BigDecimal("0");
    }

    public static String add(BigDecimal one, BigDecimal two) {
        return one.add(two).toPlainString();
    }

    public static String substract(BigDecimal one, BigDecimal two) {
        return one.subtract(two).toPlainString();
    }

}
