package com.botomo;

import java.util.Objects;

/**
 * Collection of string utility methods
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        return s == null || Objects.equals(s, "");
    }
}
