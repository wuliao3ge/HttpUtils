package com.yy.yhttputils.utils;

/**
 * Created by ly on 18-2-8.
 */

public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null || "null".equals(str) || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNoEmpty(String str)
    {
        return !isEmpty(str);
    }
}
