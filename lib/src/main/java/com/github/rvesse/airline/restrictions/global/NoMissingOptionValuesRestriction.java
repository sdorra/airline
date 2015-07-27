package com.github.rvesse.airline.restrictions.global;

import com.github.rvesse.airline.Context;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseOptionMissingValueException;
import com.github.rvesse.airline.restrictions.GlobalRestriction;

public class NoMissingOptionValuesRestriction implements GlobalRestriction {

    @Override
    public <T> void validate(ParseState<T> state) {
        if (state.getLocation() == Context.OPTION) {
            throw new ParseOptionMissingValueException(state.getCurrentOption().getTitle());
        }
    }
}
