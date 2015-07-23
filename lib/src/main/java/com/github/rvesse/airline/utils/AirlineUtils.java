package com.github.rvesse.airline.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IteratorUtils;

public class AirlineUtils {

    public static <T> List<T> arrayToList(T[] array) {
        return IteratorUtils.toList(IteratorUtils.arrayIterator(array));
    }

    public static <T> T first(Iterable<T> iterable) {
        return first(iterable.iterator(), null);
    }

    public static <T> T first(Iterator<T> iterator) {
        return first(iterator, null);
    }

    public static <T> T first(Iterable<T> iterable, T defaultValue) {
        return first(iterable.iterator(), defaultValue);
    }

    public static <T> T first(Iterator<T> iter, T defaultValue) {
        if (iter.hasNext()) {
            return iter.next();
        } else {
            return defaultValue;
        }
    }

    public static <T> T last(Iterable<T> iterable) {
        return last(iterable.iterator(), null);
    }

    public static <T> T last(Iterator<T> iterator) {
        return last(iterator, null);
    }

    public static <T> T last(Iterable<T> iterable, T defaultValue) {
        return last(iterable.iterator(), defaultValue);
    }

    public static <T> T last(Iterator<T> iter, T defaultValue) {
        T value = defaultValue;
        while (iter.hasNext()) {
            value = iter.next();
        }
        return value;
    }

    public static <T> List<T> singletonList(T item) {
        List<T> list = new ArrayList<T>();
        list.add(item);
        return list;
    }

    public static <T> Set<T> singletonSet(T item) {
        Set<T> set = new HashSet<T>();
        set.add(item);
        return set;
    }

    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> intersection = new HashSet<T>();
        for (T item : a) {
            if (b.contains(item))
                intersection.add(item);
        }
        return intersection;
    }
}
