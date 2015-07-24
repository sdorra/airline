package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.model.OptionMetadata;
import com.github.rvesse.airline.parser.ParseState;

public interface OptionRestriction {

    public abstract <T> void postValidate(ParseState<T> state, OptionMetadata option);

    public abstract <T> void preValidate(ParseState<T> state, OptionMetadata option, String value);
}