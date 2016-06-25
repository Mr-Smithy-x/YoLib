package com.mrsmyx.yorehab.utils;

/**
 * Created by cj on 1/24/16.
 */
public class Utils {
    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
