package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;

public abstract class AbstractRestriction implements GlobalRestriction, OptionRestriction, ArgumentsRestriction {

    @Override
    public <T> void validate(ParseState<T> state) {
        // Does no validation
    }

    @Override
    public <T> void postValidate(ParseState<T> state, OptionMetadata option) {
        // Does no validation
    }
    
    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        // Does no validation
    }

    @Override
    public <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        // Does no validation
    }

    @Override
    public <T> void postValidate(ParseState<T> state, ArgumentsMetadata arguments) {
        // Does no validation
    }
}
