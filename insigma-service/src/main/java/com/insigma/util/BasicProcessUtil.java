package main.java.com.insigma.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @ClassName BasicProcessUtil
 * @Description
 * @Author carrots
 * @Date 2022/6/20 13:26
 * @Version 1.0
 */
public class BasicProcessUtil {
    public static <T> Predicate<T> distinctBykey(Function<? super T, Object> keyExtract) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return  new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return seen.putIfAbsent(keyExtract.apply(t), Boolean.TRUE) == null;
            }
        };
//                t -> seen.putIfAbsent(keyExtract.apply(t), Boolean.TRUE) == null;
    }
}
