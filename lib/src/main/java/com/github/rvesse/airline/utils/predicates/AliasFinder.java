package com.github.rvesse.airline.utils.predicates;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.github.rvesse.airline.model.AliasMetadata;

public class AliasFinder implements Predicate<AliasMetadata> {
    
    private final String name;
    
    public AliasFinder(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(AliasMetadata alias) {
        return alias != null && StringUtils.equals(this.name, alias.getName());
    }

}
