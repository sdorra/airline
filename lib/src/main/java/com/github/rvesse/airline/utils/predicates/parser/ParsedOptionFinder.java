package com.github.rvesse.airline.utils.predicates.parser;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.tuple.Pair;

import com.github.rvesse.airline.model.OptionMetadata;

public class ParsedOptionFinder implements Predicate<Pair<OptionMetadata, Object>> {
    
    private final OptionMetadata opt;
    
    public ParsedOptionFinder(OptionMetadata option) {
        this.opt = option;
    }

    @Override
    public boolean evaluate(Pair<OptionMetadata, Object> parsedOption) {
        if (parsedOption == null) return false;
        if (this.opt == null) return false;
        
        return this.opt.equals(parsedOption.getLeft());
    }

}
