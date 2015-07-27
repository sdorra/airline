package com.github.rvesse.airline.utils.predicates.parser;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.CommandGroupMetadata;

public class GroupFinder implements Predicate<CommandGroupMetadata> {

    private final String name;
    
    public GroupFinder(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(CommandGroupMetadata group) {
        return group != null && StringUtils.equals(this.name, group.getName());
    }
}
