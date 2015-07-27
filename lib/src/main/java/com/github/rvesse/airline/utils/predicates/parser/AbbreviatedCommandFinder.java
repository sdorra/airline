package com.github.rvesse.airline.utils.predicates.parser;

import java.util.Collection;

import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.utils.predicates.AbstractAbbreviationFinder;

public final class AbbreviatedCommandFinder extends AbstractAbbreviationFinder<CommandMetadata> {

    public AbbreviatedCommandFinder(String cmd, Collection<CommandMetadata> commands) {
        super(cmd, commands);
    }

    @Override
    protected boolean isExactNameMatch(String value, CommandMetadata item) {
        return item.getName().equals(value);
    }

    @Override
    protected boolean isPartialNameMatch(String value, CommandMetadata item) {
        return item.getName().startsWith(value);
    }
}
