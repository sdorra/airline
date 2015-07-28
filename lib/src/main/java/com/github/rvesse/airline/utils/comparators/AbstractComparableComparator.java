package com.github.rvesse.airline.utils.comparators;

public class AbstractComparableComparator<T extends Comparable<T>> extends AbstractObjectComparator<T > {

    public AbstractComparableComparator(Class<T> cls) {
        super(cls);
    }

    @Override
    protected final int compareValues(T v1, T v2) {
        return v1.compareTo(v2);
    }

}
