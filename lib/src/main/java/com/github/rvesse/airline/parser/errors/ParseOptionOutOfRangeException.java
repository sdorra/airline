package com.github.rvesse.airline.parser.errors;

/**
 * Error that indicates that an options value was outside of an acceptable range
 *
 */
public class ParseOptionOutOfRangeException extends ParseRestrictionViolatedException {
    private static final long serialVersionUID = 4391651222234661142L;

    private final Object illegalValue, min, max;
    private final boolean minInclusive, maxInclusive;

    /**
     * Creates a new out of range exception
     * 
     * @param optionTitle
     *            Option title
     * @param value
     *            The value which is out of range
     * @param min
     *            Minimum value which may be null for no minimum
     * @param minInclusive
     *            Whether the minimum value is inclusive
     * @param max
     *            Maximum value which may be null for no maximum
     * @param maxInclusive
     *            Whether the maximum value is inclusive
     */
    public ParseOptionOutOfRangeException(String optionTitle, Object value, Object min, boolean minInclusive,
            Object max, boolean maxInclusive) {
        super("Value for option '%s' was given as '%s' which is not in the acceptable range: %s", optionTitle, value,
                range(min, minInclusive, max, maxInclusive));
        this.illegalValue = value;
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Formats the range for display
     * 
     * @param min
     *            Minimum (may be null for no minimum)
     * @param minInclusive
     *            Whether the minimum is inclusive
     * @param max
     *            Maximum (may be null for no maximum)
     * @param maxInclusive
     *            Whether the maximum is inclusive
     * @return Human readable range
     */
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
