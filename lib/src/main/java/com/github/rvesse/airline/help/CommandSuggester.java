package com.github.rvesse.airline.help;

import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.model.ParserMetadata;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;

public class CommandSuggester
        implements Suggester
{
    @Inject
    public CommandMetadata command;

    @Override
    public Iterable<String> suggest()
    {
        ImmutableList.Builder<String> suggestions = ImmutableList.<String>builder()
                .addAll(concat(transform(command.getCommandOptions(), OptionMetadata.optionsGetter())));

        if (command.getArguments() != null) {
            // Include arguments separator
            suggestions.add(ParserMetadata.DEFAULT_ARGUMENTS_SEPARATOR);
        }

        return suggestions.build();
    }
}
