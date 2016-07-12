package com.botomo;

import java.util.Objects;

/**
 * Collection of string utilitily methods
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String s) {
        return Objects.equals(s, "");
    }
}
