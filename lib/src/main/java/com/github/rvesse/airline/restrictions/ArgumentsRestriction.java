package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.parser.ParseState;

public interface ArgumentsRestriction {

    
    public abstract <T> void validate(ParseState<T> state, ArgumentsMetadata arguments);
}