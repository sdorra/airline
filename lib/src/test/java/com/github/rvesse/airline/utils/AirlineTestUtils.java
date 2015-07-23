package com.github.rvesse.airline.utils;

public class AirlineTestUtils {

    @SafeVarargs
    public static <T> T firstNonNull(T... args) {
        for (T arg : args) {
            if (arg != null)
                return arg;
        }
        return null;
    }
    
    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(obj.getClass());
    }
}
