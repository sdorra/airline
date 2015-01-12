package io.airlift.airline.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import io.airlift.airline.Accessor;
import io.airlift.airline.Group;

import java.util.List;
import java.util.Map;

public class CommandMetadata {
    private final String name;
    private final String description;
    private final boolean hidden;
    private final List<OptionMetadata> globalOptions;
    private final List<OptionMetadata> groupOptions;
    private final List<OptionMetadata> commandOptions;
    private final ArgumentsMetadata arguments;
    private final List<Accessor> metadataInjections;
    private final Class<?> type;
    private final List<String> groupNames;
    private final List<Group> groups;
    private final Map<Integer, String> exitCodes;

    private final List<String> examples;
    private final String discussion;

    public CommandMetadata(String name, String description, final String discussion, final List<String> examples,
            boolean hidden, Iterable<OptionMetadata> globalOptions, Iterable<OptionMetadata> groupOptions,
            Iterable<OptionMetadata> commandOptions, ArgumentsMetadata arguments,
            Iterable<Accessor> metadataInjections, Class<?> type, List<String> groupNames, List<Group> groups,
            Map<Integer, String> exitCodes) {
        this.name = name;
        this.description = description;
        this.hidden = hidden;
        this.globalOptions = ImmutableList.copyOf(globalOptions);
        this.groupOptions = ImmutableList.copyOf(groupOptions);
        this.commandOptions = ImmutableList.copyOf(commandOptions);
        this.arguments = arguments;
        this.metadataInjections = ImmutableList.copyOf(metadataInjections);
        this.type = type;

        this.discussion = discussion;
        this.examples = examples;

        this.groupNames = groupNames;
        this.groups = groups;

        this.exitCodes = ImmutableMap.copyOf(exitCodes);
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

    public List<OptionMetadata> getAllOptions() {
        return ImmutableList.<OptionMetadata> builder().addAll(globalOptions).addAll(groupOptions)
                .addAll(commandOptions).build();
    }

    public List<String> getExamples() {
        return examples;
    }

    public String getDiscussion() {
        return discussion;
    }

    public List<OptionMetadata> getGlobalOptions() {
        return globalOptions;
    }

    public List<OptionMetadata> getGroupOptions() {
        return groupOptions;
    }

    public List<OptionMetadata> getCommandOptions() {
        return commandOptions;
    }

    public ArgumentsMetadata getArguments() {
        return arguments;
    }

    public List<Accessor> getMetadataInjections() {
        return metadataInjections;
    }

    public Class<?> getType() {
        return type;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Map<Integer, String> getExitCodes() {
        return exitCodes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CommandMetadata");
        sb.append("{name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", discussion='").append(discussion).append('\'');
        sb.append(", examples='").append(examples).append('\'');
        sb.append(", globalOptions=").append(globalOptions);
        sb.append(", groupOptions=").append(groupOptions);
        sb.append(", commandOptions=").append(commandOptions);
        sb.append(", arguments=").append(arguments);
        sb.append(", metadataInjections=").append(metadataInjections);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }

    public static Function<CommandMetadata, String> nameGetter() {
        return new Function<CommandMetadata, String>() {
            public String apply(CommandMetadata input) {
                return input.getName();
            }
        };
    }

    @SuppressWarnings("rawtypes")
    public static Function<CommandMetadata, Class> typeGetter() {
        return new Function<CommandMetadata, Class>() {
            public Class<?> apply(CommandMetadata input) {
                return input.getType();
            }
        };
    }
}
