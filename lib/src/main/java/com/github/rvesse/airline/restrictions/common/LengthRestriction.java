package com.github.rvesse.airline.restrictions.common;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.utils.AirlineUtils;

public class LengthRestriction extends AbstractStringRestriction {

    private final boolean maximum;
    private final int length;

    public LengthRestriction(int length, boolean maximum) {
        this.length = length;
        this.maximum = maximum;
    }

    @Override
    protected boolean isValid(String value) {
        if (maximum) {
            return value.length() <= this.length;
        } else {
            return value.length() > this.length;
        }
    }

    @Override
    protected <T> ParseRestrictionViolatedException violated(ParseState<T> state, OptionMetadata option, String value) {
        if (maximum) {
            return new ParseRestrictionViolatedException(
                    "Option '%s' was given value '%s' that has length %d which exceeds the maximum permitted length of %d",
                    option.getTitle(), value, value.length(), this.length);
        } else {
            return new ParseRestrictionViolatedException(
                    "Option '%s' was given value '%s' that has length %d which is below the minimum required length of %d",
                    option.getTitle(), value, value.length(), this.length);
        }
    }

    @Override
    protected <T> ParseRestrictionViolatedException violated(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        if (maximum) {
            return new ParseRestrictionViolatedException(
                    "Argument '%s' was given value '%s' that has length %d which exceeds the maximum permitted length of %d",
                    AirlineUtils.first(arguments.getTitle()), value, value.length(), this.length);

        } else {
            return new ParseRestrictionViolatedException(
                    "Argument '%s' was given value '%s' that has length %d which is below the minimum required length of %d",
                    AirlineUtils.first(arguments.getTitle()), value, value.length(), this.length);
        }
    }

}
