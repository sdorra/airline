package com.github.rvesse.airline.utils.predicates;

import org.apache.commons.collections4.Predicate;

import com.github.rvesse.airline.model.OptionMetadata;

public class OptionFinder implements Predicate<OptionMetadata> {
    
    private final String name;
    
    public OptionFinder(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(OptionMetadata option) {
        return option != null && option.getOptions().contains(this.name);
    }

}
