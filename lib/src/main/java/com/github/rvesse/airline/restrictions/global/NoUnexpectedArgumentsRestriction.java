package com.github.rvesse.airline.restrictions.global;

import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseArgumentsUnexpectedException;
import com.github.rvesse.airline.restrictions.GlobalRestriction;

public class NoUnexpectedArgumentsRestriction implements GlobalRestriction {

    @Override
    public <T> void validate(ParseState<T> state) {
        if (!state.getUnparsedInput().isEmpty()) {
            throw new ParseArgumentsUnexpectedException(state.getUnparsedInput());
        }
    }

}
