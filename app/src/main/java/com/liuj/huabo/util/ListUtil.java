package com.liuj.huabo.util;

import java.util.List;

public class ListUtil {

    /**
     * 判断集合是否为空
     *
     * @param <T>
     * @param list
     * @return
     */
    public static <T> boolean isEmpty(List<T> list) {
        return !(list != null && list.size() > 0);
    }


    public static <T> void setNull(List<T> list) {
        if (!isEmpty(list)) {
            list.clear();
            list = null;
        }
    }

    public static <T> boolean contains(List<T> list, T t) {
        for (T item : list) {
            if (item.equals(t)) {
                return true;
            }
        }
        return false;
    }


    public static String[] listToArray(List<String> data){
      return   data.toArray(new String[data.size()]);
    }

}
