package com.test.analysis.utils;

/**
 * Created by Administrator on 2017/8/18.
 */

public class Log {
    public static void d(String s) {
        System.out.println(s);
    }
    public static void d(String tag, String s) {
        System.out.println(s);
    }
    public static void e(String tag, String s) {
        System.err.println(s);
    }
}
