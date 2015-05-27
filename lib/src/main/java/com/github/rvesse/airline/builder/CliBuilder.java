package com.github.rvesse.airline.builder;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.CommandFactory;
import com.github.rvesse.airline.CommandFactoryDefault;
import com.github.rvesse.airline.TypeConverter;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class CliBuilder<C> extends AbstractBuilder<Cli<C>> {

    protected final String name;
    protected String description;
    protected TypeConverter typeConverter = new TypeConverter();
    protected String optionSeparators;
    private Class<? extends C> defaultCommand;
    private final List<Class<? extends C>> defaultCommandGroupCommands = newArrayList();
    protected final Map<String, AliasBuilder<C>> aliases = newHashMap();
    protected final Map<String, GroupBuilder<C>> groups = newHashMap();
    protected CommandFactory<C> commandFactory = new CommandFactoryDefault<C>();
    protected boolean allowAbbreviatedCommands, allowAbbreviatedOptions;

    public CliBuilder(String name) {
        checkNotBlank(name, "Program name");
        this.name = name;
    }

    public CliBuilder<C> withDescription(String description) {
        checkNotEmpty(description, "Description");
        this.description = description;
        return this;
    }

    public CliBuilder<C> withCommandFactory(CommandFactory<C> commandFactory) {
        this.commandFactory = commandFactory;
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

    public AliasBuilder<C> withAlias(final String name) {
        checkNotBlank(name, "Alias name");

        if (aliases.containsKey(name)) {
            return aliases.get(name);
        }

        AliasBuilder<C> alias = new AliasBuilder<C>(name);
        aliases.put(name, alias);
        return alias;
    }

    public AliasBuilder<C> getAlias(final String name) {
        checkNotBlank(name, "Alias name");
        Preconditions.checkArgument(aliases.containsKey(name), "Alias %s has not been declared", name);

        return aliases.get(name);
    }

    public CliBuilder<C> withCommandAbbreviation() {
        this.allowAbbreviatedCommands = true;
        return this;
    }

    public CliBuilder<C> withOptionAbbreviation() {
        this.allowAbbreviatedOptions = true;
        return this;
    }

    @Override
    public Cli<C> build() {
        return new Cli<C>(name, description, typeConverter, defaultCommand, commandFactory,
                defaultCommandGroupCommands, groups.values(), aliases.values(), allowAbbreviatedCommands,
                allowAbbreviatedOptions);
    }
}
