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

import com.github.rvesse.airline.builder.AliasBuilder;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.builder.GroupBuilder;
import com.github.rvesse.airline.model.AliasMetadata;
import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseArgumentsMissingException;
import com.github.rvesse.airline.parser.ParseArgumentsUnexpectedException;
import com.github.rvesse.airline.parser.ParseCommandMissingException;
import com.github.rvesse.airline.parser.ParseCommandUnrecognizedException;
import com.github.rvesse.airline.parser.ParseOptionMissingException;
import com.github.rvesse.airline.parser.ParseOptionMissingValueException;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.Parser;
import com.github.rvesse.airline.parser.ParserUtil;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

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

    private final GlobalMetadata metadata;

    private final CommandFactory<C> mCommandFactory;

    /**
     * Creates a new CLI
     * 
     * @param name
     *            Program Name
     * @param description
     *            Program Description
     * @param typeConverter
     *            Type converter used to convert arguments into the Java types
     *            that the options expect
     * @param defaultCommand
     *            Default command
     * @param theCommandFactory
     *            Command factory
     * @param defaultGroupCommands
     *            Commands in the default group i.e. top level commands
     * @param groups
     *            Command groups
     * @param aliases
     *            Command aliases
     * @param allowAbbreviatedCommands
     *            Whether command abbreviation is allowed
     * @param allowAbbreviatedOptions
     *            Whethr option abbreviation is allowed
     */
    public Cli(String name, String description, TypeConverter typeConverter, Class<? extends C> defaultCommand,
            CommandFactory<C> theCommandFactory, Iterable<Class<? extends C>> defaultGroupCommands,
            Iterable<GroupBuilder<C>> groups, Iterable<AliasBuilder<C>> aliases, boolean aliasesOverrideBuiltIns,
            boolean allowAbbreviatedCommands, boolean allowAbbreviatedOptions) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name) && !StringUtils.isWhitespace(name),
                "Program name cannot be null/empty/whitespace");
        Preconditions.checkNotNull(typeConverter, "typeConverter is null");
        Preconditions.checkNotNull(theCommandFactory, "theCommandFactory is null");

        mCommandFactory = theCommandFactory;

        CommandMetadata defaultCommandMetadata = null;
        if (defaultCommand != null) {
            defaultCommandMetadata = MetadataLoader.loadCommand(defaultCommand);
        }

        final List<CommandMetadata> allCommands = new ArrayList<CommandMetadata>();

        List<CommandMetadata> defaultCommandGroup = defaultGroupCommands != null ? Lists.newArrayList(MetadataLoader
                .loadCommands(defaultGroupCommands)) : Lists.<CommandMetadata> newArrayList();

        // Currently the default command is required to be in the commands
        // list. If that changes, we'll need to add it here and add checks for
        // existence
        allCommands.addAll(defaultCommandGroup);

        // Build groups
        List<CommandGroupMetadata> commandGroups;
        if (groups != null) {
            commandGroups = Lists.newArrayList(Iterables.transform(groups,
                    new Function<GroupBuilder<C>, CommandGroupMetadata>() {

                        @Override
                        public CommandGroupMetadata apply(GroupBuilder<C> group) {
                            return group.build();
                        }

                    }));
        } else {
            commandGroups = Lists.newArrayList();
        }
        for (CommandGroupMetadata group : commandGroups) {
            allCommands.addAll(group.getCommands());
        }

        // add commands to groups based on the value of groups in the @Command
        // annotations
        // rather than change the entire way metadata is loaded, I figured just
        // post-processing was an easier, yet uglier, way to go
        MetadataLoader.loadCommandsIntoGroupsByAnnotation(allCommands, commandGroups, defaultCommandGroup);

        // Build aliases
        List<AliasMetadata> aliasData;
        if (aliases != null) {
            aliasData = Lists.newArrayList(Iterables.transform(aliases, new Function<AliasBuilder<C>, AliasMetadata>() {

                @Override
                public AliasMetadata apply(AliasBuilder<C> input) {
                    return input.build();
                }

            }));
        } else {
            aliasData = Lists.newArrayList();
        }

        Preconditions.checkArgument(allCommands.size() > 0, "Must specify at least one command to create a CLI");

        this.metadata = MetadataLoader.loadGlobal(name, description, defaultCommandMetadata,
                ImmutableList.copyOf(defaultCommandGroup), ImmutableList.copyOf(commandGroups),
                ImmutableList.copyOf(aliasData), aliasesOverrideBuiltIns, allowAbbreviatedCommands,
                allowAbbreviatedOptions);
    }

    public GlobalMetadata getMetadata() {
        return metadata;
    }

    public C parse(CommandFactory<C> commandFactory, String... args) {
        return parse(commandFactory, ImmutableList.copyOf(args));
    }

    public C parse(String... args) {
        return parse(mCommandFactory, ImmutableList.copyOf(args));
    }

    public C parse(Iterable<String> args) {
        return parse(mCommandFactory, args);
    }

    public C parse(CommandFactory<C> commandFactory, Iterable<String> args) {
        Preconditions.checkNotNull(args, "args is null");

        Parser parser = new Parser();
        ParseState state = parser.parse(metadata, args);

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

        Parser parser = new Parser();
        ParseState state = parser.parse(metadata, args);

        CommandMetadata command = MetadataLoader.loadCommand(commandInstance.getClass());

        state = state.withCommand(command);

        validate(state);

        ImmutableMap.Builder<Class<?>, Object> bindings = ImmutableMap.<Class<?>, Object> builder().put(
                GlobalMetadata.class, metadata);

        if (state.getGroup() != null) {
            bindings.put(CommandGroupMetadata.class, state.getGroup());
        }

        bindings.put(CommandMetadata.class, command);

        C c = (C) ParserUtil.injectOptions(commandInstance, command.getAllOptions(), state.getParsedOptions(),
                command.getArguments(), state.getParsedArguments(), command.getMetadataInjections(), bindings.build());

        return c;
    }

    private void validate(ParseState state) {
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
