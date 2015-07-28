package com.github.rvesse.airline.parser.errors;

/**
 * Exception class that indicates that some restriction was violated
 */
public class ParseRestrictionViolatedException extends ParseException {
    private static final long serialVersionUID = -3269082489946417712L;

    public ParseRestrictionViolatedException(String message, Object... args) {
        super(message, args);
    }

    public ParseRestrictionViolatedException(Exception cause, String message, Object... args) {
        super(cause, message, args);
    }

}
