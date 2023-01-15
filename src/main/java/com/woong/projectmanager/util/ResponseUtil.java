package com.woong.projectmanager.util;

public class ResponseUtil {
    public static String checkNull(String str, String replaceStr) {
        return (str == null || str.equals("")) ? replaceStr : str;
    }

    public static String checkNull(String str) {
        return (str == null || str.equals("")) ? "" : str;
    }
}
