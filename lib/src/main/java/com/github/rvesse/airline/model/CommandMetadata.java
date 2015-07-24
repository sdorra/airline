package com.github.rvesse.airline.model;

import com.github.rvesse.airline.Accessor;
import com.github.rvesse.airline.annotations.Group;
import com.github.rvesse.airline.utils.AirlineUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;

public class CommandMetadata {
    private final String name;
    private final String description;
    private final boolean hidden;
    private final List<OptionMetadata> globalOptions;
    private final List<OptionMetadata> groupOptions;
    private final List<OptionMetadata> commandOptions;
    private final OptionMetadata defaultOption;
    private final ArgumentsMetadata arguments;
    private final List<Accessor> metadataInjections;
    private final Class<?> type;
    private final List<String> groupNames;
    private final List<Group> groups;
    private final Map<Integer, String> exitCodes;

    private final List<String> examples;
    private final List<String> discussion;

    //@formatter:off
    public CommandMetadata(String name, 
                           String description, 
                           final List<String> discussion, 
                           final List<String> examples,
                           boolean hidden, 
                           Iterable<OptionMetadata> globalOptions, 
                           Iterable<OptionMetadata> groupOptions,
                           Iterable<OptionMetadata> commandOptions, 
                           OptionMetadata defaultOption,
                           ArgumentsMetadata arguments,
                           Iterable<Accessor> metadataInjections, 
                           Class<?> type, 
                           List<String> groupNames, 
                           List<Group> groups,
                           Map<Integer, String> exitCodes) {
    //@formatter:on
        this.name = name;
        this.description = description;
        this.hidden = hidden;
        this.globalOptions = AirlineUtils.unmodifiableListCopy(globalOptions);
        this.groupOptions = AirlineUtils.unmodifiableListCopy(groupOptions);
        this.commandOptions = AirlineUtils.unmodifiableListCopy(commandOptions);
        this.defaultOption = defaultOption;
        this.arguments = arguments;
        
        if (this.defaultOption != null && this.arguments != null) {
            throw new IllegalArgumentException("Command cannot declare both @Arguments and @DefaultOption");
        }
        
        this.metadataInjections = AirlineUtils.unmodifiableListCopy(metadataInjections);
        this.type = type;

        this.discussion = discussion;
        this.examples = examples;

        this.groupNames = groupNames;
        this.groups = groups;

        this.exitCodes = AirlineUtils.unmodifiableMapCopy(exitCodes);
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
        List<OptionMetadata> allOptions = new ArrayList<OptionMetadata>();
        allOptions.addAll(globalOptions);
        allOptions.addAll(groupOptions);
        allOptions.addAll(commandOptions);
        return ListUtils.unmodifiableList(allOptions);
    }

    public List<String> getExamples() {
        return examples;
    }

    public List<String> getDiscussion() {
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
    
    public OptionMetadata getDefaultOption() {
        return defaultOption;
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
}
