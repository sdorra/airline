package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.Comparator;

import com.github.rvesse.airline.annotations.restrictions.IntegerRange;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.RangeRestriction;

public class IntegerRangeRestrictionFactory implements OptionRestrictionFactory {
    
    private static final Comparator<Object> COMPARATOR = new IntegerComparator();

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        if (annotation instanceof IntegerRange) {
            IntegerRange range = (IntegerRange) annotation;
            
            return new RangeRestriction(Integer.valueOf(range.min()), range.minInclusive(), Integer.valueOf(range.max()), range.maxInclusive(), COMPARATOR);
        }
        return null;
    }

    private static class IntegerComparator implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == o2) return 0;
            
            if (o1 == null) {
                if (o2 == null) return 0;
                return -1;
            } else if (o2 == null) {
                return 1;
            }
            
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return Integer.compare(((Integer)o1).intValue(), ((Integer)o2).intValue());
            }
            return 0;
        }
        
    }
}
