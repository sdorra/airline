package com.github.rvesse.airline.examples.userguide.practise;

import com.github.rvesse.airline.ConvertResult;
import com.github.rvesse.airline.DefaultTypeConverter;

/**
 * An example of an extended type converter that adds support for converting
 * from types that provide an {@code parse(String)} method
 *
 */
public class ExtendedTypeConverter extends DefaultTypeConverter {

    @Override
    public Object convert(String name, Class<?> type, String value) {
        checkArguments(name, type, value);

        // Try and convert from a parse(String) method
        ConvertResult result = this.tryConvertStringMethod(type, value, "parse");
        if (result.wasSuccessfull())
            return result.getConvertedValue();

        // Fall back to default behaviour otherwise
        return super.convert(name, type, value);
    }
}
