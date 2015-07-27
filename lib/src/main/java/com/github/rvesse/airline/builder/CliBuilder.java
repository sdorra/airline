package com.github.rvesse.airline.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.ListUtils;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.MetadataLoader;
import com.github.rvesse.airline.restrictions.GlobalRestriction;
import com.github.rvesse.airline.restrictions.None;
import com.github.rvesse.airline.utils.AirlineUtils;

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
    protected final List<Class<? extends C>> defaultCommandGroupCommands = new ArrayList<>();
    protected final Map<String, GroupBuilder<C>> groups = new HashMap<>();
    protected final List<GlobalRestriction> restrictions = new ArrayList<>();
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
        this.defaultCommandGroupCommands.addAll(ListUtils.unmodifiableList(IteratorUtils.toList(IteratorUtils
                .arrayIterator(moreCommands))));
        return this;
    }

    public CliBuilder<C> withCommands(Iterable<Class<? extends C>> commands) {
        this.defaultCommandGroupCommands.addAll(ListUtils.unmodifiableList(IteratorUtils.toList(commands.iterator())));
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
        if (!groups.containsKey(name))
            throw new IllegalArgumentException(String.format("Group %s has not been declared", name));

        return groups.get(name);
    }

    public CliBuilder<C> withRestriction(GlobalRestriction restriction) {
        if (restriction != null)
            restrictions.add(restriction);
        return this;
    }

    public CliBuilder<C> withRestrictions(GlobalRestriction... restrictions) {
        for (GlobalRestriction restriction : restrictions) {
            if (restriction == null)
                continue;
            this.restrictions.add(restriction);
        }
        return this;
    }

    public CliBuilder<C> withNoRestrictions() {
        restrictions.clear();
        restrictions.add(new None());
        return this;
    }

    public CliBuilder<C> withDefaultRestrictions() {
        restrictions.addAll(AirlineUtils.arrayToList(GlobalRestriction.DEFAULTS));
        return this;
    }

    public CliBuilder<C> withOnlyDefaultRestrictions() {
        restrictions.clear();
        return withDefaultRestrictions();
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

        List<CommandMetadata> allCommands = new ArrayList<CommandMetadata>();

        List<CommandMetadata> defaultCommandGroup = defaultCommandGroupCommands != null ? MetadataLoader
                .loadCommands(defaultCommandGroupCommands) : new ArrayList<CommandMetadata>();

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
            commandGroups = new ArrayList<>();
        }
        for (CommandGroupMetadata group : commandGroups) {
            allCommands.addAll(group.getCommands());
        }

        // add commands to groups based on the value of groups in the @Command
        // annotations
        // rather than change the entire way metadata is loaded, I figured just
        // post-processing was an easier, yet uglier, way to go
        MetadataLoader.loadCommandsIntoGroupsByAnnotation(allCommands, commandGroups, defaultCommandGroup);

        // Build restrictions
        // Use defaults if none specified
        if (restrictions.size() == 0)
            withDefaultRestrictions();

        if (allCommands.size() == 0)
            throw new IllegalArgumentException("Must specify at least one command to create a CLI");

        // Build metadata objects
        GlobalMetadata<C> metadata = MetadataLoader.<C> loadGlobal(name, description, defaultCommandMetadata,
                ListUtils.unmodifiableList(defaultCommandGroup), ListUtils.unmodifiableList(commandGroups),
                ListUtils.unmodifiableList(restrictions), this.parserBuilder.build());

        return new Cli<C>(metadata);
    }
}
