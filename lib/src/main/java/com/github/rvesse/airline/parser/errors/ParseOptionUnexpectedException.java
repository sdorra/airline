package com.github.rvesse.airline.parser.errors;

/**
 * Exception thrown if an option is encountered in an unexpected location e.g.
 * trying to use a complex option with GNU classic get-opt syntax
 *
 */
public class ParseOptionUnexpectedException extends ParseException {
    private static final long serialVersionUID = 3493707402382042376L;

    public ParseOptionUnexpectedException(String string, Object... args) {
        super(string, args);
    }

}
