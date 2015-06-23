package com.github.rvesse.airline.parser;

import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.DefaultTypeConverter;
import com.github.rvesse.airline.model.OptionMetadata;

/**
 * Abstract base class for parsers providing some utility methods
 */
public class AbstractParser {
    
    /**
     * Default type converter
     */
    private static final TypeConverter DEFAULT_TYPE_CONVERTER = new DefaultTypeConverter();

    /**
     * Checks for a valid value and throws an error if the value for the option
     * is restricted and not in the set of allowed values
     * 
     * @param option
     *            Option meta data
     * @param tokenStr
     *            Token string
     */
    protected final void checkValidValue(OptionMetadata option, String tokenStr) {
        if (option.getAllowedValues() == null)
            return;
        if (option.getAllowedValues().contains(tokenStr))
            return;
        throw new ParseOptionIllegalValueException(option.getTitle(), tokenStr, option.getAllowedValues());
    }

    /**
     * Gets the type converter to use for converting arguments into
     * option/argument values
     * 
     * @param state
     *            Parser State
     * @return Type converter
     */
    protected final TypeConverter getTypeConverter(ParseState state) {
        if (state != null && state.getGlobal() != null) {
            return state.getGlobal().getParserConfiguration().getTypeConverter();
        } else {
            return DEFAULT_TYPE_CONVERTER;
        }
    }

}