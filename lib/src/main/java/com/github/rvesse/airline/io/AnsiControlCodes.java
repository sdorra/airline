package com.github.rvesse.airline.io;

/**
 * Constants
 * 
 * @author rvesse
 *
 */
public class AnsiControlCodes {

    /**
     * Private constructor to prevent instantiation
     */
    AnsiControlCodes() {
    }

    /**
     * Standard ANSI escape sequence
     */
    public static final String ESCAPE = "\u001B[";

    /**
     * Character used to separate parameters in ANSI escape sequences
     */
    public static final char PARAM_SEPARATOR = ';';

    /**
     * Command code for setting the graphics rendition
     */
    public static final char SELECT_GRAPHIC_RENDITION = 'm';

    /**
     * 24 bit extended colour mode
     */
    public static final char COLOR_MODE_24_BIT = '2';

    /**
     * 256 colour extended colour mode
     */
    public static final char COLOR_MODE_256_BIT = '5';

    /**
     * Control Code for setting the foreground colour to an extended colour
     */
    public static final int FOREGROUND_EXTENDED = 38;

    /**
     * Control Code for setting the background colour to an extended colour
     */
    public static final int BACKGROUND_EXTENDED = 48;

    /**
     * Control Code for setting the foreground colour
     */
    public static final int FOREGROUND = 30;

    /**
     * Control Code for setting the background colour
     */
    public static final int BACKGROUND = 40;

    /**
     * Control code for resetting the foreground colour to the default
     */
    public static final int DEFAULT_FOREGROUND = 39;

    /**
     * Control code for resetting the background colour to the default
     */
    public static final int DEFAULT_BACKGROUND = 49;
}
