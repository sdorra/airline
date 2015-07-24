package com.github.rvesse.airline.parser.options;

import java.util.List;

import org.apache.commons.collections4.iterators.PeekingIterator;
import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * Abstract option parser for options that are specified in {@code --name=value}
 * style while the separator character (in this example {@code =}) can be
 * configured as desired.
 * 
 * @author rvesse
 *
 */
public abstract class AbstractKeyValueOptionParser<T> extends AbstractOptionParser<T> {

    private static final char DEFAULT_SEPARATOR = '=';
    private final char separator;

    public AbstractKeyValueOptionParser() {
        this(DEFAULT_SEPARATOR);
    }

    public AbstractKeyValueOptionParser(char sep) {
        this.separator = sep;
    }

    @Override
    public ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state, List<OptionMetadata> allowedOptions) {
        List<String> parts = AirlineUtils.unmodifiableListCopy(StringUtils.split(tokens.peek(), new String(new char[] { this.separator }), 2));
        if (parts.size() != 2) {
            return null;
        }

        OptionMetadata option = findOption(state, allowedOptions, parts.get(0));
        if (option == null || option.getArity() != 1) {
            // Only supported for arity 1 options currently
            return null;
        }

        // we have a match so consume the token
        tokens.next();

        // update state
        state = state.pushContext(Context.OPTION).withOption(option);
        checkValidValue(state, option, parts.get(1));
        Object value = getTypeConverter(state).convert(option.getTitle(), option.getJavaType(), parts.get(1));
        state = state.withOption(option).withOptionValue(option, value).popContext();

        return state;
    }

}
