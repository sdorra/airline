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
package com.github.rvesse.airline.parser.command;

import static com.github.rvesse.airline.parser.ParserUtil.createInstance;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.AbstractCommandParser;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.restrictions.ArgumentsRestriction;
import com.github.rvesse.airline.restrictions.GlobalRestriction;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;

public class SingleCommandParser<T> extends AbstractCommandParser<T> {

    public T parse(ParserMetadata<T> parserConfig, CommandMetadata commandMetadata,
            Iterable<GlobalRestriction> restrictions, Iterable<String> args) {
        if (args == null)
            throw new NullPointerException("args is null");

        ParseState<T> state = tryParse(parserConfig, commandMetadata, args);
        validate(state, IteratorUtils.toList(restrictions.iterator()));

        CommandMetadata command = state.getCommand();

        //@formatter:off
        return createInstance(command.getType(), 
                              command.getAllOptions(), 
                              state.getParsedOptions(),
                              command.getArguments(), 
                              state.getParsedArguments(), 
                              command.getMetadataInjections(),
                              Collections.<Class<?>, Object>unmodifiableMap(AirlineUtils.singletonMap(CommandMetadata.class, commandMetadata)),
                              state.getParserConfiguration().getCommandFactory());
        //@formatter:on
    }

    /**
     * Validates the parser state
     * <p>
     * This includes things like verifying we ended in an appropriate state,
     * that all required options and arguments were present etc
     * </p>
     * 
     * @param state
     *            Parser state
     */
    protected void validate(ParseState<T> state, List<GlobalRestriction> restrictions) {
        // Global restrictions
        for (GlobalRestriction restriction : restrictions) {
            if (restriction == null)
                continue;
            restriction.validate(state);
        }
        CommandMetadata command = state.getCommand();
        if (command != null) {
            // Arguments restrictions
            ArgumentsMetadata arguments = command.getArguments();
            if (arguments != null) {
                for (ArgumentsRestriction restriction : arguments.getRestrictions()) {
                    if (restriction == null)
                        continue;
                    restriction.postValidate(state, arguments);
                }
            }

            // Option restrictions
            for (OptionMetadata option : command.getAllOptions()) {
                if (option == null)
                    continue;
                for (OptionRestriction restriction : option.getRestrictions()) {
                    if (restriction == null)
                        continue;
                    restriction.postValidate(state, option);
                }
            }
        }
    }
}
