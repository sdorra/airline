package com.github.rvesse.airline.utils.comparators;

import java.util.Comparator;

public class StringHierarchyComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        if (o1 == o2)
            return 0;

        if (o1 == null) {
            if (o2 == null)
                return 0;
            return -1;
        } else if (o2 == null) {
            return 1;
        }
        
        int c = Integer.compare(o1.length(), o2.length());
        if (c == 0) {
            c = o1.compareTo(o2);
        }
        return c;
    }

}
