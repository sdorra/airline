package com.github.rvesse.airline.parser;

import com.github.rvesse.airline.parser.errors.ParseException;

/**
 * Error that is thrown if too many arguments are provided
 */
public class ParseTooManyArgumentsException extends ParseException {
    private static final long serialVersionUID = -4597154963755198959L;

    public ParseTooManyArgumentsException(String string, Object... args) {
        super(String.format(string, args));
    }

    public ParseTooManyArgumentsException(Exception cause, String string, Object... args) {
        super(String.format(string, args), cause);
    }
}
