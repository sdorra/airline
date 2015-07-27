package com.github.rvesse.airline.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.restrictions.GlobalRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * Represents metadata about a CLI
 */
public class GlobalMetadata<T> {

    private final String name;
    private final String description;
    private final List<OptionMetadata> options;
    private final CommandMetadata defaultCommand;
    private final List<CommandMetadata> defaultGroupCommands;
    private final List<CommandGroupMetadata> commandGroups;
    private final ParserMetadata<T> parserConfig;
    private final List<GlobalRestriction> restrictions;

    public GlobalMetadata(String name, String description, Iterable<OptionMetadata> options,
            CommandMetadata defaultCommand, Iterable<CommandMetadata> defaultGroupCommands,
            Iterable<CommandGroupMetadata> commandGroups, Iterable<GlobalRestriction> restrictions,
            ParserMetadata<T> parserConfig) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Program name cannot be null/empty/whitespace");
        if (parserConfig == null)
            throw new NullPointerException("parseConfig cannot be null");

        this.name = name;
        this.description = description;
        this.options = AirlineUtils.unmodifiableListCopy(options);
        this.defaultCommand = defaultCommand;
        this.defaultGroupCommands = AirlineUtils.unmodifiableListCopy(defaultGroupCommands);
        this.commandGroups = AirlineUtils.unmodifiableListCopy(commandGroups);
        this.restrictions = AirlineUtils.unmodifiableListCopy(restrictions);
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
    
    public List<GlobalRestriction> getRestrictions() {
        return restrictions;
    }

    public ParserMetadata<T> getParserConfiguration() {
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
