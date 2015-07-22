package com.github.rvesse.airline;

import com.github.rvesse.airline.parser.errors.ParseOptionConversionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * The default type converter
 * <p>
 * This converter supports all the basic Java types plus types. Additionally it
 * supports any class that defines a static {@code fromString String} or
 * {@code valueOf(String)} method. Finally it supports any class that defines a
 * constructor that takes a string.
 * </p>
 */
public class DefaultTypeConverter implements TypeConverter {
    @Override
    public Object convert(String name, Class<?> type, String value) {
        if (name == null) throw new NullPointerException("name is null");
        if (type == null) throw new NullPointerException("type is null");
        if (value == null) throw new NullPointerException("value is null");

        // Firstly try the standard Java types
        ConvertResult result = tryConvertBasicTypes(type, value);
        if (result.wasSuccessfull())
            return result.getConvertedValue();

        // Then look for a static fromString(String) method
        result = tryConvertFromString(type, value);
        if (result.wasSuccessfull())
            return result.getConvertedValue();

        // Then look for a static valueOf(String) method
        // This covers enums which have a valueOf method
        result = tryConvertFromValueOf(type, value);
        if (result.wasSuccessfull())
            return result.getConvertedValue();

        // Finally look for a constructor taking a string
        result = tryConvertStringConstructor(type, value);
        if (result.wasSuccessfull())
            return result.getConvertedValue();

        throw new ParseOptionConversionException(name, value, type.getSimpleName());
    }

    /**
     * Tries to convert the value by invoking a constructor that takes a string
     * on the type
     * 
     * @param type
     *            Type
     * @param value
     *            value
     * @return Conversion result
     */
    protected final ConvertResult tryConvertStringConstructor(Class<?> type, String value) {
        try {
            Constructor<?> constructor = type.getConstructor(String.class);
            return new ConvertResult(constructor.newInstance(value));
        } catch (Throwable ignored) {
        }
        return ConvertResult.FAILURE;
    }

    /**
     * Tries to convert the value by invoking a static {@code valueOf(String)}
     * method on the type
     * 
     * @param type
     *            Type
     * @param value
     *            Value
     * @return Conversion result
     */
    protected final ConvertResult tryConvertFromValueOf(Class<?> type, String value) {
        return tryConvertStringMethod(type, value, "valueOf");
    }

    /**
     * Tries to convert the value by invoking a static
     * {@code fromString(String)} method on the type
     * 
     * @param type
     *            Type
     * @param value
     *            Value
     * @return Conversion result
     */
    protected final ConvertResult tryConvertFromString(Class<?> type, String value) {
        return tryConvertStringMethod(type, value, "fromString");

    }

    /**
     * Tries to convert the value by invoking a static method on the type
     * 
     * @param type
     *            Type
     * @param value
     *            Value
     * @param methodName
     *            Name of the method to invoke
     * @return Conversion Result
     */
    protected final ConvertResult tryConvertStringMethod(Class<?> type, String value, String methodName) {
        try {
            Method method = type.getMethod(methodName, String.class);
            if (method.getReturnType().isAssignableFrom(type)) {
                return new ConvertResult(method.invoke(null, value));
            }
        } catch (Throwable ignored) {
        }
        return ConvertResult.FAILURE;
    }

    /**
     * Tries to convert the value if it is one of the common Java types
     * 
     * @param type
     *            Type
     * @param value
     *            Value
     * @return Conversion result
     */
    protected final ConvertResult tryConvertBasicTypes(Class<?> type, String value) {
        try {
            if (String.class.isAssignableFrom(type)) {
                return new ConvertResult(value);
            } else if (Boolean.class.isAssignableFrom(type) || Boolean.TYPE.isAssignableFrom(type)) {
                return new ConvertResult(Boolean.valueOf(value));
            } else if (Byte.class.isAssignableFrom(type) || Byte.TYPE.isAssignableFrom(type)) {
                return new ConvertResult(Byte.valueOf(value));
            } else if (Short.class.isAssignableFrom(type) || Short.TYPE.isAssignableFrom(type)) {
                return new ConvertResult(Short.valueOf(value));
            } else if (Integer.class.isAssignableFrom(type) || Integer.TYPE.isAssignableFrom(type)) {
                return new ConvertResult(Integer.valueOf(value));
            } else if (Long.class.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type)) {
                return new ConvertResult(Long.valueOf(value));
            } else if (Float.class.isAssignableFrom(type) || Float.TYPE.isAssignableFrom(type)) {
                return new ConvertResult(Float.valueOf(value));
            } else if (Double.class.isAssignableFrom(type) || Double.TYPE.isAssignableFrom(type)) {
                return new ConvertResult(Double.valueOf(value));
            }
        } catch (Exception ignored) {
        }
        return ConvertResult.FAILURE;
    }

    private static class ConvertResult {
        private final Object value;
        private final boolean success;

        public static final ConvertResult FAILURE = new ConvertResult();

        private ConvertResult() {
            this.value = null;
            this.success = false;
        }

        public ConvertResult(Object value) {
            this.value = value;
            this.success = true;
        }

        public boolean wasSuccessfull() {
            return this.success;
        }

        public Object getConvertedValue() {
            return this.value;
        }
    }
}
