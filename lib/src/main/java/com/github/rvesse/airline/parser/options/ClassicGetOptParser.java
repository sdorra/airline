package com.github.rvesse.airline.parser.options;

import java.util.List;

import org.apache.commons.collections4.iterators.PeekingIterator;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionUnexpectedException;

/**
 * An options parsing that parses options given in classic get-opt style where
 * multiple options may be concatenated together
 * <p>
 * For example {@code -abc} could potentially set the option {@code -a},
 * {@code -b} and {@code -c} however interpretation is contextual depending on
 * the option configuration. Say option {@code -a} has arity of 1 then the
 * remainder of the token (the {@code bc}) would be interpreted as being the
 * value passed to the {@code -a} option.
 * </p>
 *
 * @param <T>
 */
public class ClassicGetOptParser<T> extends AbstractOptionParser<T> {
    public ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state,
            List<OptionMetadata> allowedOptions) {
        if (!hasShortNamePrefix(tokens.peek())) {
            return null;
        }

        // remove leading dash from token
        String remainingToken = tokens.peek().substring(1);

        ParseState<T> nextState = state;
        boolean first = true;
        while (!remainingToken.isEmpty()) {
            char tokenCharacter = remainingToken.charAt(0);

            // is the current token character a single letter option?
            OptionMetadata option = findOption(state, allowedOptions, "-" + tokenCharacter);
            if (option == null) {
                return null;
            }

            nextState = nextState.pushContext(Context.OPTION).withOption(option);

            // remove current token character
            remainingToken = remainingToken.substring(1);

            // for no argument options, process the option and remove the
            // character from the token
            if (option.getArity() == 0) {
                nextState = nextState.withOptionValue(option, Boolean.TRUE).popContext();
                first = false;
                continue;
            }

            if (option.getArity() == 1) {
                // we must, consume the current token so we can see the next
                // token
                tokens.next();

                // if current token has more characters, this is the value;
                // otherwise it is the next token
                if (!remainingToken.isEmpty()) {
                    checkValidValue(state, option, remainingToken);
                    Object value = getTypeConverter(state).convert(option.getTitle(), option.getJavaType(),
                            remainingToken);
                    nextState = nextState.withOptionValue(option, value).popContext();
                } else if (tokens.hasNext()) {
                    String tokenStr = tokens.next();
                    checkValidValue(state, option, tokenStr);
                    Object value = getTypeConverter(state).convert(option.getTitle(), option.getJavaType(), tokenStr);
                    nextState = nextState.withOptionValue(option, value).popContext();
                }

                return nextState;
            }

            // Don't throw an error if this is the first option we have seen as
            // in that case the option may legitimately be processed by another
            // option parser
            if (first)
                return null;
            throw new ParseOptionUnexpectedException("Short options style can not be used with option %s", option);
        }

        // consume the current token
        tokens.next();

        return nextState;
    }
}
