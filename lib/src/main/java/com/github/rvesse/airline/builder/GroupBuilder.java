package com.github.rvesse.airline.builder;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class GroupBuilder<C> {
    
    private final String name;
    private String description = null;
    private Class<? extends C> defaultCommand = null;

    private final List<Class<? extends C>> commands = newArrayList();

    GroupBuilder(String name) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name) && !StringUtils.isWhitespace(name),
                "Group name cannot be null/empty/whitespace");
        this.name = name;
    }

    public GroupBuilder<C> withDescription(String description) {
        Preconditions.checkNotNull(description, "description is null");
        Preconditions.checkArgument(!description.isEmpty(), "description is empty");
        Preconditions.checkState(this.description == null, "description is already set");
        this.description = description;
        return this;
    }

    public GroupBuilder<C> withDefaultCommand(Class<? extends C> defaultCommand) {
        Preconditions.checkNotNull(defaultCommand, "defaultCommand is null");
        Preconditions.checkState(this.defaultCommand == null, "defaultCommand is already set");
        this.defaultCommand = defaultCommand;
        return this;
    }

    public GroupBuilder<C> withCommand(Class<? extends C> command) {
        Preconditions.checkNotNull(command, "command is null");
        commands.add(command);
        return this;
    }

    @SuppressWarnings("unchecked")
    public GroupBuilder<C> withCommands(Class<? extends C> command, Class<? extends C>... moreCommands) {
        this.commands.add(command);
        this.commands.addAll(ImmutableList.copyOf(moreCommands));
        return this;
    }

    public GroupBuilder<C> withCommands(Iterable<Class<? extends C>> commands) {
        this.commands.addAll(ImmutableList.copyOf(commands));
        return this;
    }
    
    public CommandGroupMetadata build() {
        CommandMetadata groupDefault = MetadataLoader.loadCommand(defaultCommand);
        List<CommandMetadata> groupCommands = MetadataLoader.loadCommands(commands);

        return MetadataLoader.loadCommandGroup(name, description, groupDefault,
                groupCommands);
    }
}
