package com.github.rvesse.airline.builder;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Builder for CLIs
 *
 * @param <C>
 *            Command type
 */
public class CliBuilder<C> extends AbstractBuilder<Cli<C>> {

    protected final String name;
    protected String description;
    protected String optionSeparators;
    protected Class<? extends C> defaultCommand;
    protected final List<Class<? extends C>> defaultCommandGroupCommands = newArrayList();
    protected final Map<String, GroupBuilder<C>> groups = newHashMap();
    protected final ParserBuilder<C> parserBuilder = new ParserBuilder<C>();

    public CliBuilder(String name) {
        checkNotBlank(name, "Program name");
        this.name = name;
    }

    public CliBuilder<C> withDescription(String description) {
        checkNotEmpty(description, "Description");
        this.description = description;
        return this;
    }

    public CliBuilder<C> withDefaultCommand(Class<? extends C> defaultCommand) {
        this.defaultCommand = defaultCommand;
        return this;
    }

    public CliBuilder<C> withCommand(Class<? extends C> command) {
        this.defaultCommandGroupCommands.add(command);
        return this;
    }

    @SuppressWarnings("unchecked")
    public CliBuilder<C> withCommands(Class<? extends C> command, Class<? extends C>... moreCommands) {
        this.defaultCommandGroupCommands.add(command);
        this.defaultCommandGroupCommands.addAll(ImmutableList.copyOf(moreCommands));
        return this;
    }

    public CliBuilder<C> withCommands(Iterable<Class<? extends C>> commands) {
        this.defaultCommandGroupCommands.addAll(ImmutableList.copyOf(commands));
        return this;
    }

    public GroupBuilder<C> withGroup(String name) {
        checkNotBlank(name, "Group name");

        if (groups.containsKey(name)) {
            return groups.get(name);
        }

        GroupBuilder<C> group = new GroupBuilder<C>(name);
        groups.put(name, group);
        return group;
    }

    public GroupBuilder<C> getGroup(final String name) {
        checkNotBlank(name, "Group name");
        Preconditions.checkArgument(groups.containsKey(name), "Group %s has not been declared", name);

        return groups.get(name);
    }

    public ParserBuilder<C> withParser() {
        return parserBuilder;
    }

    @Override
    public Cli<C> build() {
        CommandMetadata defaultCommandMetadata = null;
        if (defaultCommand != null) {
            defaultCommandMetadata = MetadataLoader.loadCommand(defaultCommand);
        }

        final List<CommandMetadata> allCommands = new ArrayList<CommandMetadata>();

        List<CommandMetadata> defaultCommandGroup = defaultCommandGroupCommands != null ? Lists
                .newArrayList(MetadataLoader.loadCommands(defaultCommandGroupCommands)) : Lists
                .<CommandMetadata> newArrayList();

        // Currently the default command is required to be in the commands
        // list. If that changes, we'll need to add it here and add checks for
        // existence
        allCommands.addAll(defaultCommandGroup);

        // Build groups
        List<CommandGroupMetadata> commandGroups;
        if (groups != null) {
            commandGroups = new ArrayList<CommandGroupMetadata>();
            for (GroupBuilder<C> groupBuilder : groups.values()) {
                commandGroups.add(groupBuilder.build());
            }
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

        Preconditions.checkArgument(allCommands.size() > 0, "Must specify at least one command to create a CLI");

        // Build metadata objects
        GlobalMetadata<C> metadata = MetadataLoader.<C> loadGlobal(name, description, defaultCommandMetadata,
                ImmutableList.copyOf(defaultCommandGroup), ImmutableList.copyOf(commandGroups),
                this.parserBuilder.build());

        return new Cli<C>(metadata);
    }
}
