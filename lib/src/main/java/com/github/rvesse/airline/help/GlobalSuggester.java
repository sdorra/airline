package com.github.rvesse.airline.help;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.ListUtils;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

public class GlobalSuggester<T>
    implements Suggester
{
    @Inject
    public GlobalMetadata<T> metadata;

    @Override
    public Iterable<String> suggest()
    {
        List<String> suggestions = new ArrayList<String>();
        for (CommandGroupMetadata group : metadata.getCommandGroups()) {
            suggestions.add(group.getName());
        }
        for (CommandMetadata command : metadata.getDefaultGroupCommands()) {
            suggestions.add(command.getName());
        }
        for (OptionMetadata option : metadata.getOptions()) {
            suggestions.addAll(option.getOptions());
        }
        return ListUtils.unmodifiableList(suggestions);
    }
}
