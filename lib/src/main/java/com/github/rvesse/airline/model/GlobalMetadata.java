package com.github.rvesse.airline.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GlobalMetadata {
    private final String name;
    private final String description;
    private final List<OptionMetadata> options;
    private final CommandMetadata defaultCommand;
    private final List<CommandMetadata> defaultGroupCommands;
    private final List<CommandGroupMetadata> commandGroups;
    private final ParserMetadata parserConfig;

    public GlobalMetadata(String name, String description, Iterable<OptionMetadata> options,
            CommandMetadata defaultCommand, Iterable<CommandMetadata> defaultGroupCommands,
            Iterable<CommandGroupMetadata> commandGroups, ParserMetadata parserConfig) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name) && !StringUtils.isWhitespace(name),
                "Program name cannot be null/empty/whitespace");
        Preconditions.checkNotNull(parserConfig);
        //Preconditions.checkNotNull(typeConverter, "typeConverter is null");

        this.name = name;
        this.description = description;
        this.options = ImmutableList.copyOf(options);
        this.defaultCommand = defaultCommand;
        this.defaultGroupCommands = ImmutableList.copyOf(defaultGroupCommands);
        this.commandGroups = ImmutableList.copyOf(commandGroups);
        this.parserConfig = parserConfig;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<OptionMetadata> getOptions() {
        return options;
    }

    public CommandMetadata getDefaultCommand() {
        return defaultCommand;
    }

    public List<CommandMetadata> getDefaultGroupCommands() {
        return defaultGroupCommands;
    }

    public List<CommandGroupMetadata> getCommandGroups() {
        return commandGroups;
    }

    public ParserMetadata getParserConfiguration() {
        return parserConfig;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GlobalMetadata");
        sb.append("{name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", options=").append(options);
        sb.append(", defaultCommand=").append(defaultCommand);
        sb.append(", defaultGroupCommands=").append(defaultGroupCommands);
        sb.append(", commandGroups=").append(commandGroups);
        sb.append(", parserConfig=").append('\n').append(parserConfig);
        sb.append('\n').append('}');
        return sb.toString();
    }
}
