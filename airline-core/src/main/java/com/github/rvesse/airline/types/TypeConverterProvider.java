package com.github.rvesse.airline.types;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;

/**
 * Interface for type converter providers
 * 
 * @author rvesse
 *
 */
public interface TypeConverterProvider {

    /**
     * Gets the type converter to use for the given option and parser state
     * 
     * @param option
     *            Option
     * @param state
     *            Parser state
     * @return Type converter
     */
    public abstract <T> TypeConverter getTypeConverter(OptionMetadata option, ParseState<T> state);

    /**
     * Gets the type converter to use for the given arguments and parser state
     * 
     * @param arguments
     *            Arguments
     * @param state
     *            Parser state
     * @return Type converter
     */
    public abstract <T> TypeConverter getTypeConverter(ArgumentsMetadata arguments, ParseState<T> state);
}
