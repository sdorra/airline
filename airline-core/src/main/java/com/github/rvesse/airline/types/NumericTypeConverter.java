package com.github.rvesse.airline.types;

import com.github.rvesse.airline.parser.errors.ParseOptionConversionException;

public class NumericTypeConverter implements TypeConverter {

    @Override
    public final Object convert(String name, Class<?> type, String value) {
        DefaultTypeConverter.checkArguments(name, type, value);

        ConvertResult result = tryConvertNumerics(name, type, value);
        if (result.wasSuccessfull())
            return result.getConvertedValue();

        throw new ParseOptionConversionException(name, value, type.getSimpleName());
    }

    /**
     * Tries to convert common numeric types
     * 
     * @param type
     *            Type
     * @param value
     *            Value
     * @return Conversion result
     */
    protected ConvertResult tryConvertNumerics(String name, Class<?> type, String value) {
        try {
            if (Byte.class.isAssignableFrom(type) || Byte.TYPE.isAssignableFrom(type)) {
                return tryConvertByte(name, value);
            } else if (Short.class.isAssignableFrom(type) || Short.TYPE.isAssignableFrom(type)) {
                return tryConvertShort(name, value);
            } else if (Integer.class.isAssignableFrom(type) || Integer.TYPE.isAssignableFrom(type)) {
                return tryConvertInteger(name, value);
            } else if (Long.class.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type)) {
                return tryConvertLong(name, value);
            } else if (Float.class.isAssignableFrom(type) || Float.TYPE.isAssignableFrom(type)) {
                return tryConvertFloat(name, value);
            } else if (Double.class.isAssignableFrom(type) || Double.TYPE.isAssignableFrom(type)) {
                return tryConvertDouble(name, value);
            }
        } catch (Exception ignored) {

        }
        return ConvertResult.FAILURE;
    }

    protected ConvertResult tryConvertDouble(String name, String value) {
        return new ConvertResult(Double.valueOf(value));
    }

    protected ConvertResult tryConvertFloat(String name, String value) {
        return new ConvertResult(Float.valueOf(value));
    }

    protected ConvertResult tryConvertLong(String name, String value) {
        return new ConvertResult(Long.valueOf(value));
    }

    protected ConvertResult tryConvertInteger(String name, String value) {
        return new ConvertResult(Integer.valueOf(value));
    }

    protected ConvertResult tryConvertShort(String name, String value) {
        return new ConvertResult(Short.valueOf(value));
    }

    protected ConvertResult tryConvertByte(String name, String value) {
        return new ConvertResult(Byte.valueOf(value));
    }
}
