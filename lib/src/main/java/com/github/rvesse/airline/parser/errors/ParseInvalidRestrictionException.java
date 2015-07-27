package com.github.rvesse.airline.parser.errors;

/**
 * Parser exception that indicates that a declared restriction was invalid
 */
public class ParseInvalidRestrictionException extends ParseException {
    private static final long serialVersionUID = -4939228307213631994L;

    public ParseInvalidRestrictionException(String string, Object... args) {
        super(string, args);
    }

}
