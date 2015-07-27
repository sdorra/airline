package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.restrictions.global.CommandRequiredRestriction;
import com.github.rvesse.airline.restrictions.global.NoMissingOptionValuesRestriction;
import com.github.rvesse.airline.restrictions.global.NoUnexpectedArgumentsRestriction;

/**
 * Interface for restrictions
 */
public interface GlobalRestriction {
    
    //@formatter:off
    static final GlobalRestriction[] DEFAULTS = new GlobalRestriction[] {
        new CommandRequiredRestriction(),
        new NoUnexpectedArgumentsRestriction(),
        new NoMissingOptionValuesRestriction()
    };
    //@formatter:on
    
    /**
     * Validates the parser state
     * <p>
     * Should throw an exception if the restriction is violated, otherwise
     * should simply return
     * </p>
     * 
     * @param state
     *            Parser state
     */
    public abstract <T> void validate(ParseState<T> state);
}
