/**
 * Copyright (C) 2010-16 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rvesse.airline.examples.userguide.parser.options;

import java.util.List;

import org.apache.commons.collections4.iterators.PeekingIterator;
import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.builder.ParserBuilder;
import com.github.rvesse.airline.examples.ExampleExecutor;
import com.github.rvesse.airline.examples.simple.Simple;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingValueException;
import com.github.rvesse.airline.parser.errors.ParseOptionUnexpectedException;
import com.github.rvesse.airline.parser.options.AbstractOptionParser;

/**
 * Option parser that parses options given in JDBC connection URL style e.g.
 * {@code port=1000;user=example;}
 * 
 * @author rvesse
 *
 * @param <T>
 *            Command type
 */
public class JdbcStyleOptionParser<T> extends AbstractOptionParser<T> {

    public static final String NAME_VALUE_SEPARATOR = "=";

    public static final String OPTION_SEPARATOR = ";";

    @Override
    public ParseState<T> parseOptions(PeekingIterator<String> tokens, ParseState<T> state,
            List<OptionMetadata> allowedOptions) {
        // Peek at the first input
        String options = tokens.peek();

        // Must have at least one name=value pair for this to be a JDBC style
        // option specification
        if (!options.contains(NAME_VALUE_SEPARATOR))
            return null;

        // May potentially be multiple name value pairs in the string separated
        // by a semicolon
        String[] optionPairs = options.split(OPTION_SEPARATOR);

        // Try and parse the options
        ParseState<T> nextState = state;
        boolean first = true;
        for (String pair : optionPairs) {
            // Allow for empty pair, this may occur if the user terminates the
            // options with a semicolon which is legitimate
            if (StringUtils.isEmpty(pair))
                continue;

            if (!pair.contains(NAME_VALUE_SEPARATOR)) {
                // This would be invalid for us but if this is the first option
                // we've seen might be valid for another option parser
                if (first)
                    return null;

                // Otherwise treat as an invalid option
                state.getParserConfiguration().getErrorHandler()
                        .handleError(new ParseOptionMissingValueException(pair));
            }

            // Find the relevant option
            String[] nameValue = pair.split(NAME_VALUE_SEPARATOR, 2);
            OptionMetadata option = findOption(state, allowedOptions, nameValue[0]);
            if (option == null) {
                // No such option, let another option parser try to parse the
                // option string
                if (first)
                    return null;

                state.getParserConfiguration().getErrorHandler().handleError(new ParseOptionUnexpectedException(
                        "JDBC style option '%s' refers to option '%s' which does not refer to a known option", pair, nameValue[0]));
            }

            // Tell the parser we're parsing an option
            nextState = nextState.pushContext(Context.OPTION).withOption(option);

            if (option.getArity() == 1) {
                if (first) {
                    // If this is the first valid option we've seen we now
                    // consume the input token
                    tokens.next();
                    first = false;
                }

                // Set the option value
                nextState = nextState.withOptionValue(option, nameValue[1]).popContext();
            } else {
                // We only permit arity 1 options
                if (first)
                    return null;

                state.getParserConfiguration().getErrorHandler().handleError(new ParseOptionUnexpectedException(
                        "JDBC style option '%s' refers to option  '%s' which has arity %d, only arity 1 options are supported",
                        pair, nameValue[0], option.getArity()));
            }
        }

        // If we didn't parse anything let other parsers try
        if (first)
            return null;
        
        // Otherwise return the new state
        return nextState;
    }

    public static void main(String[] args) {
        //@formatter:off
        ParserMetadata<Simple> parserConfig 
            = new ParserBuilder<Simple>()
                .withOptionParser(new JdbcStyleOptionParser<Simple>())
                .withDefaultOptionParsers()
                .build();
        //@formatter:on
        
        ExampleExecutor.executeSingleCommand(Simple.class, parserConfig, args);
    }

}
