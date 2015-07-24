package com.github.rvesse.airline.parser.errors;

public class ParseOptionOutOfRangeException extends ParseException {
    
    private final Object illegalValue, min, max;
    private final boolean minInclusive, maxInclusive;

    public ParseOptionOutOfRangeException(String optionTitle, Object value, Object min, boolean minInclusive,
            Object max, boolean maxInclusive) {
        super("Value for option '%s' was given as '%s' which is not in the acceptable range: %s", optionTitle, range(
                min, minInclusive, max, maxInclusive));
        this.illegalValue = value;
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
    }

    private static String range(Object min, boolean minInclusive, Object max, boolean maxInclusive) {
        StringBuilder builder = new StringBuilder();

        if (min != null) {
            if (max != null) {
                // min < value < max
                builder.append(min);
                builder.append(minInclusive ? " <=" : " <");
                builder.append(" value ");
            } else {
                // value > min
                builder.append("value ");
                builder.append(minInclusive ? ">= " : ">");
                builder.append(min);
            }
        }
        if (max != null) {
            // [min <] value < max
            builder.append(maxInclusive ? "<= " : "< ");
            builder.append(max);
        }

        return builder.toString();
    }
    
    public Object getIllegalValue() {
        return this.illegalValue;
    }
    
    public Object getMinimumValue() {
        return this.min;
    }
    
    public Object getMaximumValue() {
        return this.max;
    }
    
    public boolean isMinimumInclusive() {
        return this.minInclusive;
    }
    
    public boolean isMaximumInclusive() {
        return this.maxInclusive;
    }
}
