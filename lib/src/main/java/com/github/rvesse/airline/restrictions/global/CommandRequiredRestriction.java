package com.github.rvesse.airline.restrictions.global;

import java.util.List;

import com.github.rvesse.airline.model.CommandMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseCommandMissingException;
import com.github.rvesse.airline.parser.errors.ParseCommandUnrecognizedException;
import com.github.rvesse.airline.restrictions.GlobalRestriction;

public class CommandRequiredRestriction implements GlobalRestriction {

    @Override
    public <T> void validate(ParseState<T> state) {
        CommandMetadata command = state.getCommand();
        if (command == null) {
            List<String> unparsedInput = state.getUnparsedInput();
            if (unparsedInput.isEmpty()) {
                throw new ParseCommandMissingException();
            } else {
                throw new ParseCommandUnrecognizedException(unparsedInput);
            }
        }
    }

}
