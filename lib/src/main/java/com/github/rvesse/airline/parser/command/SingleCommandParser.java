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
            restriction.validate(state);
        }
        CommandMetadata command = state.getCommand();

        // Arguments restrictions
        ArgumentsMetadata arguments = command.getArguments();
        if (arguments != null) {
            for (ArgumentsRestriction restriction : arguments.getRestrictions()) {
                restriction.postValidate(state, arguments);
            }
        }

        // Option restrictions
        for (OptionMetadata option : command.getAllOptions()) {
            for (OptionRestriction restriction : option.getRestrictions()) {
                restriction.postValidate(state, option);
            }
        }
    }
}
