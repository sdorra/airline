package com.github.rvesse.airline.restrictions;

import com.github.rvesse.airline.parser.ParseState;

/**
 * Interface for restrictions
 */
public interface GlobalRestriction {

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
