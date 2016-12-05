package com.github.rvesse.airline.types;

import com.github.rvesse.airline.parser.errors.ParseOptionConversionException;

/**
 * Abstract numeric type converter that supports numerics given in the form
 * {@code 1234suffix} where {@code suffix} denotes some multiplier. For example
 * {@code 1234k} might treat {@code k} as a multiplier of {@code 1000}. Derived
 * implementations should be created to provide the set of supported suffixes
 * and their multipliers.
 * 
 * @author rvesse
 *
 */
public abstract class AbbreviatedNumericTypeConverter extends NumericTypeConverter {

    @Override
    protected ConvertResult tryConvertDouble(String name, String value) {
        if (hasSuffix(Double.class, value)) {
            String suffix = getSuffix(Double.class, value);
            if (suffix != null) {
                long multiplier = getMultiplier(suffix);
                double base = Double.valueOf(value.substring(0, value.length() - suffix.length()));
                double result = base * multiplier;
                if (result < Double.MIN_VALUE || result > Double.MAX_VALUE)
                    throw new ParseOptionConversionException(
                            String.format(
                                    "%s: Abbreviated numeric value \"%s\" evaluates to a value outside the range of the numeric type %s",
                                    name, value, Double.class.getSimpleName()),
                            name, value, Double.class.getSimpleName());
                return new ConvertResult(result);
            }
        }
        return super.tryConvertDouble(name, value);
    }

    @Override
    protected ConvertResult tryConvertFloat(String name, String value) {
        if (hasSuffix(Float.class, value)) {
            String suffix = getSuffix(Float.class, value);
            if (suffix != null) {
                long multiplier = getMultiplier(suffix);
                double base = Float.valueOf(value.substring(0, value.length() - suffix.length()));
                double result = base * multiplier;
                if (result < Float.MIN_VALUE || result > Float.MAX_VALUE)
                    throw new ParseOptionConversionException(
                            String.format(
                                    "%s: Abbreviated numeric value \"%s\" evaluates to a value outside the range of the numeric type %s",
                                    name, value, Float.class.getSimpleName()),
                            name, value, Float.class.getSimpleName());
                return new ConvertResult((float) result);
            }
        }
        return super.tryConvertFloat(name, value);
    }

    @Override
    protected ConvertResult tryConvertLong(String name, String value) {
        if (hasSuffix(Long.class, value)) {
            String suffix = getSuffix(Long.class, value);
            if (suffix != null) {
                long multiplier = getMultiplier(suffix);
                long base = Long.valueOf(value.substring(0, value.length() - suffix.length()));
                long result = base * multiplier;
                return new ConvertResult(result);
            }
        }
        return super.tryConvertLong(name, value);
    }

    @Override
    protected ConvertResult tryConvertInteger(String name, String value) {
        if (hasSuffix(Integer.class, value)) {
            String suffix = getSuffix(Integer.class, value);
            if (suffix != null) {
                long multiplier = getMultiplier(suffix);
                long base = Integer.valueOf(value.substring(0, value.length() - suffix.length()));
                long result = base * multiplier;
                if (result < Integer.MIN_VALUE || result > Integer.MAX_VALUE)
                    throw new ParseOptionConversionException(
                            String.format(
                                    "%s: Abbreviated numeric value \"%s\" evaluates to a value outside the range of the numeric type %s",
                                    name, value, Integer.class.getSimpleName()),
                            name, value, Integer.class.getSimpleName());
                return new ConvertResult((int) result);
            }
        }
        return super.tryConvertInteger(name, value);
    }

    @Override
    protected ConvertResult tryConvertShort(String name, String value) {
        if (hasSuffix(Short.class, value)) {
            String suffix = getSuffix(Short.class, value);
            if (suffix != null) {
                long multiplier = getMultiplier(suffix);
                long base = Short.valueOf(value.substring(0, value.length() - suffix.length()));
                long result = base * multiplier;
                if (result < Short.MIN_VALUE || result > Short.MAX_VALUE)
                    throw new ParseOptionConversionException(
                            String.format(
                                    "%s: Abbreviated numeric value \"%s\" evaluates to a value outside the range of the numeric type %s",
                                    name, value, Short.class.getSimpleName()),
                            name, value, Short.class.getSimpleName());
                return new ConvertResult((short) result);
            }
        }
        return super.tryConvertShort(name, value);
    }

    @Override
    protected ConvertResult tryConvertByte(String name, String value) {
        if (hasSuffix(Byte.class, value)) {
            String suffix = getSuffix(Byte.class, value);
            if (suffix != null) {
                long multiplier = getMultiplier(suffix);
                long base = Byte.valueOf(value.substring(0, value.length() - suffix.length()));
                long result = base * multiplier;
                if (result < Byte.MIN_VALUE || result > Byte.MAX_VALUE)
                    throw new ParseOptionConversionException(String.format(
                            "%s: Abbreviated numeric value \"%s\" evaluates to a value outside the range of the numeric type %s",
                            name, value, Byte.class.getSimpleName()), name, value, Byte.class.getSimpleName());
                return new ConvertResult((byte) result);
            }
        }
        return super.tryConvertByte(name, value);
    }

    protected boolean hasSuffix(Class<?> type, String value) {
        return value.length() > 0 && !Character.isDigit(value.charAt(value.length() - 1));
    }

    protected String getSuffix(Class<?> type, String value) {
        int i;
        for (i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isDigit(c) || c == '.' || c == '+' || c == '-' || c == 'e' || c == 'E')
                continue;
            break;
        }
        if (i < value.length() - 1)
            return value.substring(i);
        return null;
    }

    protected abstract long getMultiplier(String suffix);
}
