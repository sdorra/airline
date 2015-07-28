package com.github.rvesse.airline.restrictions.factories;

import java.lang.annotation.Annotation;
import java.util.Comparator;

import com.github.rvesse.airline.annotations.restrictions.ranges.ByteRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.IntegerRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.LongRange;
import com.github.rvesse.airline.annotations.restrictions.ranges.ShortRange;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.restrictions.common.RangeRestriction;

public class RangeRestrictionFactory implements OptionRestrictionFactory, ArgumentsRestrictionFactory {

    private static final Comparator<Object> LONG_COMPARATOR = new LongComparator();
    private static final Comparator<Object> INTEGER_COMPARATOR = new IntegerComparator();
    private static final Comparator<Object> SHORT_COMPARATOR = new ShortComparator();
    private static final Comparator<Object> BYTE_COMPARATOR = new ByteComparator();

    @Override
    public OptionRestriction createOptionRestriction(Annotation annotation) {
        return createCommon(annotation);
    }
    
    @Override
    public ArgumentsRestriction createArgumentsRestriction(Annotation annotation) {
        return createCommon(annotation);
    }

    protected RangeRestriction createCommon(Annotation annotation) {
        if (annotation instanceof LongRange) {
            return createLongRange(annotation);
        } else if (annotation instanceof IntegerRange) {
            return createIntegerRange(annotation);
        } else if (annotation instanceof ShortRange) {
            return createShortRange(annotation);
        } else if (annotation instanceof ByteRange) {
            return createByteRange(annotation);
        }
        return null;
    }
    
    protected RangeRestriction createByteRange(Annotation annotation) {
        ByteRange sRange = (ByteRange) annotation;
        return new RangeRestriction(Byte.valueOf(sRange.min()), sRange.minInclusive(),
                Byte.valueOf(sRange.max()), sRange.maxInclusive(), BYTE_COMPARATOR);
    }

    protected RangeRestriction createShortRange(Annotation annotation) {
        ShortRange sRange = (ShortRange) annotation;
        return new RangeRestriction(Short.valueOf(sRange.min()), sRange.minInclusive(),
                Short.valueOf(sRange.max()), sRange.maxInclusive(), SHORT_COMPARATOR);
    }

    protected RangeRestriction createIntegerRange(Annotation annotation) {
        IntegerRange iRange = (IntegerRange) annotation;
        return new RangeRestriction(Integer.valueOf(iRange.min()), iRange.minInclusive(), Integer.valueOf(iRange
                .max()), iRange.maxInclusive(), INTEGER_COMPARATOR);
    }

    protected RangeRestriction createLongRange(Annotation annotation) {
        LongRange iRange = (LongRange) annotation;
        return new RangeRestriction(Long.valueOf(iRange.min()), iRange.minInclusive(), Long.valueOf(iRange
                .max()), iRange.maxInclusive(), LONG_COMPARATOR);
    }

    private static class LongComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == o2)
                return 0;

            if (o1 == null) {
                if (o2 == null)
                    return 0;
                return -1;
            } else if (o2 == null) {
                return 1;
            }

            if (o1 instanceof Long && o2 instanceof Long) {
                return Long.compare(((Long) o1).longValue(), ((Long) o2).longValue());
            }
            return 0;
        }
    }

    private static class IntegerComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == o2)
                return 0;

            if (o1 == null) {
                if (o2 == null)
                    return 0;
                return -1;
            } else if (o2 == null) {
                return 1;
            }

            if (o1 instanceof Integer && o2 instanceof Integer) {
                return Integer.compare(((Integer) o1).intValue(), ((Integer) o2).intValue());
            }
            return 0;
        }
    }

    private static class ShortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == o2)
                return 0;

            if (o1 == null) {
                if (o2 == null)
                    return 0;
                return -1;
            } else if (o2 == null) {
                return 1;
            }

            if (o1 instanceof Short && o2 instanceof Short) {
                return Short.compare(((Short) o1).shortValue(), ((Short) o2).shortValue());
            }
            return 0;
        }
    }

    private static class ByteComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == o2)
                return 0;

            if (o1 == null) {
                if (o2 == null)
                    return 0;
                return -1;
            } else if (o2 == null) {
                return 1;
            }

            if (o1 instanceof Byte && o2 instanceof Byte) {
                return Byte.compare(((Byte) o1).byteValue(), ((Byte) o2).byteValue());
            }
            return 0;
        }
    }
}
