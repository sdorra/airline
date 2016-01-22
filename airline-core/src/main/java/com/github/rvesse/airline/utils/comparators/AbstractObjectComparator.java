/**
 * Copyright (C) 2010-16 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rvesse.airline.utils.comparators;

import java.util.Comparator;

public abstract class AbstractObjectComparator<T> implements Comparator<Object> {

    private final Class<T> cls;

    public AbstractObjectComparator(Class<T> cls) {
        if (cls == null)
            throw new NullPointerException("cls cannot be null");
        this.cls = cls;
    }

    @Override
    public final int compare(Object o1, Object o2) {
        if (o1 == o2)
            return 0;

        if (o1 == null) {
            if (o2 == null)
                return 0;
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        Class<?> c1 = o1.getClass();
        Class<?> c2 = o2.getClass();
        if (this.cls.isAssignableFrom(c1)) {
            T v1 = tryCast(o1);
            if (this.cls.isAssignableFrom(c2)) {
                // Both same type so compare by value
                T v2 = tryCast(o2);
                return comparePossibleValues(v1, v2);
            } else {
                // Consider first greater since it is of the desired type
                return 1;
            }
        } else if (this.cls.isAssignableFrom(c2)) {
            // Consider second greater since it is of the desired type
            return -1;
        } else {
            // Compare by class
            return compareClasses(c1, c2);
        }
    }

    protected int compareClasses(Class<?> c1, Class<?> c2) {
        if (c1 == c2)
            return 0;
        if (c1.getCanonicalName() == null) {
            if (c2.getCanonicalName() == null)
                return 0;
            return -1;
        } else if (c2.getCanonicalName() == null) {
            return 1;
        }

        return c1.getCanonicalName().compareTo(c2.getCanonicalName());
    }

    protected T tryCast(Object obj) {
        try {
            return this.cls.cast(obj);
        } catch (ClassCastException e) {
            return null;
        }
    }

    private int comparePossibleValues(T v1, T v2) {
        if (v1 == v2)
            return 0;

        if (v1 == null) {
            if (v2 == null)
                return 0;
            return -1;
        } else if (v2 == null) {
            return 1;
        }
        
        return compareValues(v1, v2);
    }

    protected abstract int compareValues(T v1, T v2);

}
