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
     * Character used to request reset 
     */
    public static final char RESET = '0';

    /**
     * Command code for setting the graphics rendition
     */
    public static final char SELECT_GRAPHIC_RENDITION = 'm';

    /**
     * 24 bit extended colour mode
     */
    public static final char COLOR_MODE_TRUE = '2';

    /**
     * 256 colour extended colour mode
     */
    public static final char COLOR_MODE_256 = '5';

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
     * Control Code for setting the high intensity variant of the foreground colour
     */
    public static final int FOREGROUND_BRIGHT = 90;

    /**
     * Control Code for setting the background colour
     */
    public static final int BACKGROUND = 40;
    
    /**
     * Control Code for setting the high intensity variant of the background colour
     */
    public static final int BACKGROUND_BRIGHT = 100;

    /**
     * Control code for resetting the foreground colour to the default
     */
    public static final int DEFAULT_FOREGROUND = 39;

    /**
     * Control code for resetting the background colour to the default
     */
    public static final int DEFAULT_BACKGROUND = 49;
}
