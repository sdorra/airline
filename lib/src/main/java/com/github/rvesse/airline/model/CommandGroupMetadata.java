package com.github.rvesse.airline.model;

import java.util.List;

import com.github.rvesse.airline.utils.AirlineUtils;

public class CommandGroupMetadata {
    private final String name;
    private final String description;
    private final boolean hidden;
    private final List<OptionMetadata> options;
    private final CommandMetadata defaultCommand;
    private final List<CommandMetadata> commands;

    public CommandGroupMetadata(String name, String description, boolean hidden, Iterable<OptionMetadata> options,
            CommandMetadata defaultCommand, Iterable<CommandMetadata> commands) {
        this.name = name;
        this.description = description;
        this.hidden = hidden;
        this.options = AirlineUtils.unmodifiableListCopy(options);
        this.defaultCommand = defaultCommand;
        this.commands = AirlineUtils.listCopy(commands);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHidden() {
        return hidden;
    }

    public List<OptionMetadata> getOptions() {
        return options;
    }

    public CommandMetadata getDefaultCommand() {
        return defaultCommand;
    }

    public List<CommandMetadata> getCommands() {
        return AirlineUtils.unmodifiableListCopy(commands);
    }

    public void addCommand(CommandMetadata command) {
        if (!commands.contains(command)) {
            commands.add(command);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CommandGroupMetadata");
        sb.append("{name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", hidden=").append(hidden);
        sb.append(", options=").append(options);
        sb.append(", defaultCommand=").append(defaultCommand);
        sb.append(", commands=").append(commands);
        sb.append('}');
        return sb.toString();
    }
}
