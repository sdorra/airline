package com.github.rvesse.airline.parser;

import java.util.Collection;

import com.github.rvesse.airline.model.CommandGroupMetadata;

public final class AbbreviatedGroupFinder extends AbstractAbbreviationFinder<CommandGroupMetadata> {

    public AbbreviatedGroupFinder(String cmd, Collection<CommandGroupMetadata> groups) {
        super(cmd, groups);
    }

    @Override
    protected boolean isExactNameMatch(String value, CommandGroupMetadata item) {
        return item.getName().equals(value);
    }

    @Override
    protected boolean isPartialNameMatch(String value, CommandGroupMetadata item) {
        return item.getName().startsWith(value);
    }
}
