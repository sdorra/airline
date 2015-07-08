package com.github.rvesse.airline.parser.options;

import java.util.List;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.PeekingIterator;

/**
 * An options parser that expects the name and value(s) to be white space
 * separated e.g. {@code --name value}
 *
 */
public class StandardOptionParser<T> extends AbstractOptionParser<T> {

    @Override
    public ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state, List<OptionMetadata> allowedOptions) {
        OptionMetadata option = findOption(state, allowedOptions, tokens.peek());
        if (option == null) {
            return null;
        }

        tokens.next();
        state = state.pushContext(Context.OPTION).withOption(option);

        Object value;
        if (option.getArity() == 0) {
            state = state.withOptionValue(option, Boolean.TRUE).popContext();
        } else if (option.getArity() == 1) {
            if (tokens.hasNext()) {
                String tokenStr = tokens.next();
                checkValidValue(option, tokenStr);
                value = getTypeConverter(state).convert(option.getTitle(), option.getJavaType(), tokenStr);
                state = state.withOptionValue(option, value).popContext();
            }
        } else {
            ImmutableList.Builder<Object> values = ImmutableList.builder();

            int count = 0;

            boolean hasSeparator = false;
            boolean foundNextOption = false;
            String argsSeparator = state.getParserConfiguration().getArgumentsSeparator();
            while (count < option.getArity() && tokens.hasNext() && !hasSeparator) {
                String peekedToken = tokens.peek();
                hasSeparator = peekedToken.equals(argsSeparator);
                foundNextOption = findOption(state, allowedOptions, peekedToken) != null;

                if (hasSeparator || foundNextOption)
                    break;
                String tokenStr = tokens.next();
                checkValidValue(option, tokenStr);
                values.add(getTypeConverter(state).convert(option.getTitle(), option.getJavaType(), tokenStr));
                ++count;
            }

            if (count == option.getArity() || hasSeparator || foundNextOption) {
                state = state.withOptionValue(option, values.build()).popContext();
            }
        }
        return state;
    }

}
