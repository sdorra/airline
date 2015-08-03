package com.github.rvesse.airline.restrictions.common;

import java.util.regex.Pattern;

import com.github.rvesse.airline.model.ArgumentsMetadata;
import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseRestrictionViolatedException;
import com.github.rvesse.airline.restrictions.AbstractCommonRestriction;
import com.github.rvesse.airline.utils.AirlineUtils;

/**
 * A restriction which requires the raw values to match a given regular
 * expression
 */
public class PatternRestriction extends AbstractCommonRestriction {

    private final Pattern pattern;

    public PatternRestriction(String pattern, int flags) {
        this.pattern = Pattern.compile(pattern, flags);
    }

    @Override
    public <T> void preValidate(ParseState<T> state, OptionMetadata option, String value) {
        if (!this.pattern.matcher(value).find())
            throw new ParseRestrictionViolatedException(
                    "Option '%s' was given value '%s' which does not match the regular expression '%s'",
                    option.getTitle(), value, this.pattern.toString());
    }

    @Override
    public <T> void preValidate(ParseState<T> state, ArgumentsMetadata arguments, String value) {
        if (!this.pattern.matcher(value).find())
            throw new ParseRestrictionViolatedException(
                    "Argument '%s' was given value '%s' which does not match the regular expression '%s'",
                    AirlineUtils.first(arguments.getTitle()), value, this.pattern.toString());
    }

}
