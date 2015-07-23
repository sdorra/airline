package com.github.rvesse.airline.help;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.ListUtils;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

public class GroupSuggester implements Suggester {
    @Inject
    public CommandGroupMetadata group;

    @Override
    public Iterable<String> suggest() {
        List<String> suggestions = new ArrayList<String>();
        for (CommandMetadata command : group.getCommands()) {
            suggestions.add(command.getName());
        }
        for (OptionMetadata option : group.getOptions()) {
            suggestions.addAll(option.getOptions());
        }
        return ListUtils.unmodifiableList(suggestions);
    }
}
