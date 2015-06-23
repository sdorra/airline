package com.github.rvesse.airline;

import com.github.rvesse.airline.parser.errors.ParseOptionConversionException;

/**
 * Interface for type converters
 * <p>
 * Type converters are used to convert the string values provided as
 * option/argument values into appropriately typed values that can be assigned
 * to the relevant option/arguments
 * </p>
 *
 */
public interface TypeConverter {

    /**
     * Convert a string value into an appropriately typed value
     * 
     * @param name
     *            Option Name
     * @param type
     *            Target Type
     * @param value
     *            String Value
     * @return Typed value
     * @exception NullPointerException
     *                Should be thrown if any of the provided arguments is null
     * @exception ParseOptionConversionException
     *                Should be thrown if the type converter cannot convert the
     *                value
     */
    public abstract Object convert(String name, Class<?> type, String value);

}