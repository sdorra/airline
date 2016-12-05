package com.github.rvesse.airline.types;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.ParseState;

/**
 * Default type converter provider which simply inspects the {@link ParseState}
 * given and returns the the type converter specified on the
 * {@link ParserMetadata} provided by the parse state
 * 
 * @author rvesse
 *
 */
public class DefaultTypeConverterProvider implements TypeConverterProvider {

    @Override
    public <T> TypeConverter getTypeConverter(OptionMetadata option, ParseState<T> state) {
        return state.getParserConfiguration().getTypeConverter();
    }

    @Override
    public <T> TypeConverter getTypeConverter(ArgumentsMetadata arguments, ParseState<T> state) {
        return state.getParserConfiguration().getTypeConverter();
    }

}
