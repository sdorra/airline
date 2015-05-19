package com.github.rvesse.airline.help;

import javax.inject.Inject;

import com.github.rvesse.airline.model.CommandGroupMetadata;
import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.model.OptionMetadata;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;

public class GlobalSuggester
    implements Suggester
{
    @Inject
    public GlobalMetadata metadata;

    @Override
    public Iterable<String> suggest()
    {
        return concat(
                transform(metadata.getCommandGroups(), CommandGroupMetadata.nameGetter()),
                transform(metadata.getDefaultGroupCommands(), CommandMetadata.nameGetter()),
                concat(transform(metadata.getOptions(), OptionMetadata.optionsGetter()))
        );
    }
}
