package com.github.rvesse.airline.utils.predicates.parser;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.CommandMetadata;

public class CommandFinder implements Predicate<CommandMetadata> {

    private final String name;

    public CommandFinder(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(CommandMetadata command) {
        return command != null && StringUtils.equals(this.name, command.getName());
    }

}
