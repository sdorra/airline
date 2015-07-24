package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.parser.ParseState;

public interface ArgumentsRestriction {

    public abstract <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value);
    
    public abstract <T> void postValidate(ParseState<T> state, ArgumentsMetadata arguments);
}