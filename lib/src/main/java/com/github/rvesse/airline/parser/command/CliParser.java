package com.github.rvesse.airline.parser.command;

import static com.github.rvesse.airline.parser.ParserUtil.createInstance;

import java.util.List;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.AbstractCommandParser;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseArgumentsMissingException;
import com.github.rvesse.airline.parser.errors.ParseArgumentsUnexpectedException;
import com.github.rvesse.airline.parser.errors.ParseCommandMissingException;
import com.github.rvesse.airline.parser.errors.ParseCommandUnrecognizedException;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingException;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingValueException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public class CliParser<T> extends AbstractCommandParser<T> {

    public T parse(GlobalMetadata<T> metadata, Iterable<String> args) {
        Preconditions.checkNotNull(args, "args is null");

        ParseState<T> state = tryParse(metadata, args);

        // If we did not find a command choose the appropriate default command
        // (if any)
        if (state.getCommand() == null) {
            if (state.getGroup() != null) {
                state = state.withCommand(state.getGroup().getDefaultCommand());
            } else {
                state = state.withCommand(metadata.getDefaultCommand());
            }
        }

        validate(state);

        CommandMetadata command = state.getCommand();

        ImmutableMap.Builder<Class<?>, Object> bindings = ImmutableMap.<Class<?>, Object> builder().put(
                GlobalMetadata.class, metadata);

        if (state.getGroup() != null) {
            bindings.put(CommandGroupMetadata.class, state.getGroup());
        }

        if (state.getCommand() != null) {
            bindings.put(CommandMetadata.class, state.getCommand());
        }
        
        bindings.put(ParserMetadata.class, state.getParserConfiguration());

        //@formatter:off
        return createInstance(command.getType(), 
                              command.getAllOptions(), 
                              state.getParsedOptions(),
                              command.getArguments(), 
                              state.getParsedArguments(), 
                              command.getMetadataInjections(), 
                              bindings.build(),
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
            if (option.isRequired() && !state.getParsedOptions().containsKey(option)) {
                throw new ParseOptionMissingException(option.getOptions().iterator().next());
            }
        }
    }
}
