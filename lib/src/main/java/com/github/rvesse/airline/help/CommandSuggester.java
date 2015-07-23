package com.github.rvesse.airline.help;

import java.util.ArrayList;
import java.util.List;

import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;

import javax.inject.Inject;

import org.apache.commons.collections4.ListUtils;

public class CommandSuggester
        implements Suggester
{
    @Inject
    public CommandMetadata command;

    @Override
    public Iterable<String> suggest()
    {
        List<String> suggestions = new ArrayList<String>();
        for (OptionMetadata option : command.getCommandOptions()) {
            suggestions.addAll(option.getOptions());
        }

        if (command.getArguments() != null) {
            // Include arguments separator
            suggestions.add(ParserMetadata.DEFAULT_ARGUMENTS_SEPARATOR);
        }

        return ListUtils.unmodifiableList(suggestions);
    }
}
