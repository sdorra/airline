package com.github.rvesse.airline.parser;

import com.github.rvesse.airline.TypeConverter;
import com.github.rvesse.airline.DefaultTypeConverter;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;

/**
 * Abstract base class for parsers providing some utility methods
 */
public class AbstractParser<T> {
    
    /**
     * Default type converter
     */
    private static final TypeConverter DEFAULT_TYPE_CONVERTER = new DefaultTypeConverter();
    
    protected final void checkValidValue(ParseState<T> state, ArgumentsMetadata args, String tokenStr) {
        for (ArgumentsRestriction restriction : args.getRestrictions()) {
            restriction.preValidate(state, args, tokenStr);
        }
    }

    /**
     * Checks for a valid value and throws an error if the value for the option
     * fails a restriction
     * 
     * @param state Parser state
     * @param option
     *            Option meta data
     * @param tokenStr
     *            Token string
     */
    protected final void checkValidValue(ParseState<T> state, OptionMetadata option, String tokenStr) {
        for (OptionRestriction restriction : option.getRestrictions()) {
            restriction.preValidate(state, option, tokenStr);
        }
    }

    /**
     * Gets the type converter to use for converting arguments into
     * option/argument values
     * 
     * @param state
     *            Parser State
     * @return Type converter
     */
    protected final TypeConverter getTypeConverter(ParseState<T> state) {
        if (state != null) {
            return state.getParserConfiguration().getTypeConverter();
        } else {
            return DEFAULT_TYPE_CONVERTER;
        }
    }

}