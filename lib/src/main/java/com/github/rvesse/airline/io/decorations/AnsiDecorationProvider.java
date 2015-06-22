package com.github.rvesse.airline.io.decorations;

/**
 * Interface for classes that provide ANSI decorations
 *
 */
public interface AnsiDecorationProvider {

    /**
     * Gets the ANSI control code for enabling a decoration
     * 
     * @return Control code
     */
    public abstract String getAnsiDecorationEnabledControlCode();

    /**
     * Gets the ANSI control code for disabling a decoration
     * 
     * @return Control Code
     */
    public abstract String getAnsiDecorationDisabledControlCode();

}
