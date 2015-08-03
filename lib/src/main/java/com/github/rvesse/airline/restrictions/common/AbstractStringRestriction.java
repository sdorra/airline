package com.github.rvesse.airline.restrictions.common;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;

public abstract class AbstractStringRestriction extends AbstractCommonRestriction {

    @Override
    public final <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        if (!isValid(value))
            throw violated(state, option, value);
    }

    @Override
    public final <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        if (!isValid(value))
            throw violated(state, arguments, value);
    }

    protected abstract boolean isValid(String value);

    protected abstract <T> ParseRestrictionViolatedException violated(ParseState<T> state, OptionMetadata option,
            String value);

    protected abstract <T> ParseRestrictionViolatedException violated(ParseState<T> state, ArgumentsMetadata arguments,
            String value);
}
