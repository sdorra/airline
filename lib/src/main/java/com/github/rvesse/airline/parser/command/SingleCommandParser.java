package com.github.rvesse.airline.parser.command;

import static com.github.rvesse.airline.parser.ParserUtil.createInstance;

import java.util.Collections;
import java.util.List;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.AbstractCommandParser;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseArgumentsMissingException;
import com.github.rvesse.airline.parser.errors.ParseArgumentsUnexpectedException;
import com.github.rvesse.airline.parser.errors.ParseCommandMissingException;
import com.github.rvesse.airline.parser.errors.ParseCommandUnrecognizedException;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingValueException;
import com.github.rvesse.airline.restrictions.OptionRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;

public class SingleCommandParser<T> extends AbstractCommandParser<T> {

    public T parse(ParserMetadata<T> parserConfig, CommandMetadata commandMetadata, Iterable<String> args) {
        if (args == null)
            throw new NullPointerException("args is null");

        ParseState<T> state = tryParse(parserConfig, commandMetadata, args);
        validate(state);

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
    protected void validate(ParseState<T> state) {
        CommandMetadata command = state.getCommand();
        if (command == null) {
            List<String> unparsedInput = state.getUnparsedInput();
            if (unparsedInput.isEmpty()) {
                throw new ParseCommandMissingException();
            } else {
                throw new ParseCommandUnrecognizedException(unparsedInput);
            }
        }

        ArgumentsMetadata arguments = command.getArguments();
        if (state.getParsedArguments().isEmpty() && arguments != null && arguments.isRequired()) {
            throw new ParseArgumentsMissingException(arguments.getTitle());
        }

        if (!state.getUnparsedInput().isEmpty()) {
            throw new ParseArgumentsUnexpectedException(state.getUnparsedInput());
        }

        if (state.getLocation() == Context.OPTION) {
            throw new ParseOptionMissingValueException(state.getCurrentOption().getTitle());
        }

        for (OptionMetadata option : command.getAllOptions()) {
            for (OptionRestriction restriction : option.getRestrictions()) {
                restriction.postValidate(state, option);
            }
        }
    }
}
