package cn.hjf.job.email.utils;

import java.util.Collection;

public class CollectionUtil {
    /**
     * 判断集合是否不为空
     *
     * @param collection 需要判断的集合
     * @return 如果集合不为空，返回 true；否则返回 false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
