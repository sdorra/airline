/**
 * Copyright (C) 2010 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
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

package com.github.rvesse.airline;

import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.github.rvesse.airline.parser.ParseArgumentsMissingException;
import com.github.rvesse.airline.parser.ParseArgumentsUnexpectedException;
import com.github.rvesse.airline.parser.ParseCommandMissingException;
import com.github.rvesse.airline.parser.ParseCommandUnrecognizedException;
import com.github.rvesse.airline.parser.ParseOptionMissingException;
import com.github.rvesse.airline.parser.ParseOptionMissingValueException;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.Parser;
import com.github.rvesse.airline.parser.ParserUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import static com.github.rvesse.airline.parser.ParserUtil.createInstance;

public class Cli<C> {
    /**
     * Creates a builder for specifying a command line in fluent style
     * 
     * @param name
     *            Program name
     * @return CLI Builder
     */
    public static <T> CliBuilder<T> builder(String name) {
        Preconditions.checkNotNull(name, "name is null");
        return new CliBuilder<T>(name);
    }

    private final GlobalMetadata<C> metadata;

    /**
     * Creates a new CLI
     * 
     * @param metadata
     *            Metadata
     */
    public Cli(GlobalMetadata<C> metadata) {
        Preconditions.checkNotNull(metadata);
        this.metadata = metadata;
    }

    public GlobalMetadata<C> getMetadata() {
        return metadata;
    }

    public C parse(CommandFactory<C> commandFactory, String... args) {
        return parse(commandFactory, ImmutableList.copyOf(args));
    }

    public C parse(String... args) {
        return parse(metadata.getParserConfiguration().getCommandFactory(), ImmutableList.copyOf(args));
    }

    public C parse(Iterable<String> args) {
        return parse(metadata.getParserConfiguration().getCommandFactory(), args);
    }

    public C parse(CommandFactory<C> commandFactory, Iterable<String> args) {
        Preconditions.checkNotNull(args, "args is null");

        Parser<C> parser = new Parser<C>();
        ParseState<C> state = parser.parse(metadata, args);

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

        return createInstance(command.getType(), command.getAllOptions(), state.getParsedOptions(),
                command.getArguments(), state.getParsedArguments(), command.getMetadataInjections(), bindings.build(),
                commandFactory);
    }

    public C parse(C commandInstance, String... args) {
        Preconditions.checkNotNull(args, "args is null");

        Parser<C> parser = new Parser<C>();
        ParseState<C> state = parser.parse(metadata, args);

        CommandMetadata command = MetadataLoader.loadCommand(commandInstance.getClass());

        state = state.withCommand(command);

        validate(state);

        ImmutableMap.Builder<Class<?>, Object> bindings = ImmutableMap.<Class<?>, Object> builder().put(
                GlobalMetadata.class, metadata);
        bindings.put(ParserMetadata.class, metadata.getParserConfiguration());

        if (state.getGroup() != null) {
            bindings.put(CommandGroupMetadata.class, state.getGroup());
        }

        bindings.put(CommandMetadata.class, command);

        C c = (C) ParserUtil.injectOptions(commandInstance, command.getAllOptions(), state.getParsedOptions(),
                command.getArguments(), state.getParsedArguments(), command.getMetadataInjections(), bindings.build());

        return c;
    }

    private void validate(ParseState<C> state) {
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
